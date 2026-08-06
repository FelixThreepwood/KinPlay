package com.kinplay.app.feedback

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.core.content.edit
import com.kinplay.app.BuildConfig
import java.time.ZoneId
import java.util.Collections
import java.util.IdentityHashMap
import java.util.UUID

const val FEEDBACK_RECIPIENT = "FelixThreepwood@gmail.com"

/** Legacy v2 payloads do not record their creation build, so migration must preserve it as unknown. */
internal fun decodeStoredFeedback(encoded: String): List<FeedbackNote> = FeedbackCodec.decode(encoded)

class FeedbackStore(context: Context) {
    private val preferences = context.getSharedPreferences("kinplay_feedback", Context.MODE_PRIVATE)

    fun load(): List<FeedbackNote> {
        val encoded = preferences.getString(KEY_PENDING_NOTES, "").orEmpty()
        val notes = decodeStoredFeedback(encoded)
        // Rewrite the previous v2 payload after a successful read so lifecycle and revision
        // metadata become durable without dropping locally captured beta notes.
        if (notes.isNotEmpty() && encoded != FeedbackCodec.encode(notes)) save(notes)
        return notes
    }

    fun save(notes: List<FeedbackNote>) {
        preferences.edit { putString(KEY_PENDING_NOTES, FeedbackCodec.encode(notes)) }
    }

    companion object {
        private const val KEY_PENDING_NOTES = "pending_notes_v1"
    }
}

fun newFeedbackNoteId(): String = "KP-NOTE-${UUID.randomUUID().toString().uppercase()}"

fun newFeedbackBatchId(): String = "KP-BATCH-${UUID.randomUUID().toString().uppercase()}"

fun currentFeedbackBuildContext(): FeedbackBuildContext = FeedbackBuildContext(
    packageName = BuildConfig.APPLICATION_ID,
    versionName = BuildConfig.VERSION_NAME,
    versionCode = BuildConfig.VERSION_CODE,
    device = listOf(Build.MANUFACTURER, Build.MODEL).filter { it.isNotBlank() }.joinToString(" "),
    androidVersion = Build.VERSION.RELEASE.orEmpty(),
    sdkLevel = Build.VERSION.SDK_INT,
)

fun createFeedbackNote(
    type: FeedbackType,
    impact: FeedbackImpact,
    comment: String,
    expectedResult: String,
    includeTechnicalContext: Boolean,
    screen: String,
    contentId: String?,
    contentTitle: String?,
    attachments: List<FeedbackAttachment> = emptyList(),
): FeedbackNote = FeedbackNote(
    id = newFeedbackNoteId(),
    type = type,
    impact = impact,
    comment = comment.trim(),
    expectedResult = expectedResult.trim(),
    includeTechnicalContext = includeTechnicalContext,
    screen = screen,
    contentId = contentId,
    contentTitle = contentTitle,
    createdAtEpochMillis = System.currentTimeMillis(),
    timezoneId = ZoneId.systemDefault().id,
    createdInVersionName = BuildConfig.VERSION_NAME,
    createdInVersionCode = BuildConfig.VERSION_CODE,
    attachments = attachments.filter(FeedbackAttachment::isWithinPolicy).take(MAX_FEEDBACK_ATTACHMENTS),
)

fun inspectFeedbackAttachment(context: Context, uri: Uri): FeedbackAttachment? {
    val mimeType = context.contentResolver.getType(uri)?.lowercase()
    if (!isAllowedFeedbackAttachmentMimeType(mimeType)) return null
    var displayName: String? = null
    var sizeBytes: Long? = null
    context.contentResolver.query(
        uri,
        arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE),
        null,
        null,
        null,
    )?.use { cursor ->
        if (cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            displayName = nameIndex.takeIf { it >= 0 }?.let(cursor::getString)
            sizeBytes = sizeIndex.takeIf { it >= 0 && !cursor.isNull(it) }?.let(cursor::getLong)
        }
    }
    val attachment = FeedbackAttachment(
        uri = uri.toString(),
        displayName = displayName?.takeIf(String::isNotBlank) ?: uri.lastPathSegment.orEmpty(),
        mimeType = mimeType.orEmpty(),
        sizeBytes = sizeBytes ?: return null,
    )
    return attachment.takeIf(FeedbackAttachment::isWithinPolicy)
}

fun buildFeedbackMailtoUriString(recipient: String, subject: String, body: String): String =
    "mailto:$recipient?subject=${percentEncode(subject)}&body=${percentEncode(body)}"

private fun percentEncode(value: String): String = buildString {
    val hex = "0123456789ABCDEF"
    value.toByteArray(Charsets.UTF_8).forEach { byte ->
        val unsigned = byte.toInt() and 0xff
        if (
            unsigned in 'a'.code..'z'.code ||
            unsigned in 'A'.code..'Z'.code ||
            unsigned in '0'.code..'9'.code ||
            unsigned == '-'.code || unsigned == '.'.code || unsigned == '_'.code || unsigned == '~'.code
        ) {
            append(unsigned.toChar())
        } else {
            append('%')
            append(hex[unsigned shr 4])
            append(hex[unsigned and 0x0f])
        }
    }
}

private fun Context.hasActivityInBaseChain(): Boolean {
    val visited = Collections.newSetFromMap(IdentityHashMap<Context, Boolean>())
    var current: Context? = this
    while (current != null && visited.add(current)) {
        if (current is Activity) return true
        current = (current as? ContextWrapper)?.baseContext
    }
    return false
}

fun handOffFeedbackEmail(context: Context, notes: List<FeedbackNote>, batchId: String): Boolean {
    val unsentNotes = notes.filter { it.lifecycleState == FeedbackLifecycleState.UNSENT }
    if (unsentNotes.isEmpty()) return false
    val build = currentFeedbackBuildContext()
    val body = FeedbackEmailFormatter.formatBatch(unsentNotes, build, batchId)
    val attachments = unsentNotes.flatMap { it.attachments }
        .distinctBy(FeedbackAttachment::uri)
        .filter(FeedbackAttachment::isWithinPolicy)
    val intent = if (attachments.isEmpty()) {
        Intent(
            Intent.ACTION_SENDTO,
            Uri.parse(buildFeedbackMailtoUriString(FEEDBACK_RECIPIENT, FeedbackEmailFormatter.subject(build.versionName, build.versionCode, batchId), body)),
        )
    } else {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(FEEDBACK_RECIPIENT))
            putExtra(Intent.EXTRA_SUBJECT, FeedbackEmailFormatter.subject(build.versionName, build.versionCode, batchId))
            putExtra(Intent.EXTRA_TEXT, body)
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(attachments.map { Uri.parse(it.uri) }))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val first = attachments.first()
            clipData = ClipData.newUri(context.contentResolver, first.displayName, Uri.parse(first.uri))
            attachments.drop(1).forEach { attachment ->
                clipData?.addItem(ClipData.Item(Uri.parse(attachment.uri)))
            }
        }
    }.apply {
        if (!context.hasActivityInBaseChain()) addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    return try {
        context.startActivity(intent)
        true
    } catch (_: RuntimeException) {
        false
    }
}

fun copyFeedbackBatch(context: Context, notes: List<FeedbackNote>, batchId: String) {
    val build = currentFeedbackBuildContext()
    val body = FeedbackEmailFormatter.formatBatch(
        notes.filter { it.lifecycleState == FeedbackLifecycleState.UNSENT },
        build,
        batchId,
    )
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("KinPlay feedback", body))
}
