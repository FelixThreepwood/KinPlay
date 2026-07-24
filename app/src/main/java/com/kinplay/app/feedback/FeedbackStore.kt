package com.kinplay.app.feedback

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.edit
import com.kinplay.app.BuildConfig
import java.time.ZoneId
import java.util.UUID

const val FEEDBACK_RECIPIENT = "FelixThreepwood@gmail.com"

class FeedbackStore(context: Context) {
    private val preferences = context.getSharedPreferences("kinplay_feedback", Context.MODE_PRIVATE)

    fun load(): List<FeedbackNote> = FeedbackCodec.decode(
        preferences.getString(KEY_PENDING_NOTES, "").orEmpty(),
    ).sortedBy { it.createdAtEpochMillis }

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
)

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

fun handOffFeedbackEmail(context: Context, notes: List<FeedbackNote>, batchId: String): Boolean {
    if (notes.isEmpty()) return false
    val build = currentFeedbackBuildContext()
    val uri = Uri.parse(
        buildFeedbackMailtoUriString(
            recipient = FEEDBACK_RECIPIENT,
            subject = FeedbackEmailFormatter.subject(build.versionName, build.versionCode, batchId),
            body = FeedbackEmailFormatter.formatBatch(notes, build, batchId),
        ),
    )
    return try {
        context.startActivity(Intent(Intent.ACTION_SENDTO, uri))
        true
    } catch (_: ActivityNotFoundException) {
        false
    }
}

fun copyFeedbackBatch(context: Context, notes: List<FeedbackNote>, batchId: String) {
    val build = currentFeedbackBuildContext()
    val body = FeedbackEmailFormatter.formatBatch(notes, build, batchId)
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("KinPlay feedback", body))
}
