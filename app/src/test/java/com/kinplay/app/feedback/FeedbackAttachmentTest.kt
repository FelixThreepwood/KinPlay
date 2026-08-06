package com.kinplay.app.feedback

import android.app.Activity
import android.content.Intent
import android.net.Uri
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [35])
class FeedbackAttachmentTest {
    private val attachment = FeedbackAttachment(
        uri = "content://com.example.provider/screenshot.png",
        displayName = "screenshot.png",
        mimeType = "image/png",
        sizeBytes = 12_345,
    )

    private val note = FeedbackNote(
        id = "KP-NOTE-ATTACHMENT",
        type = FeedbackType.BUG,
        impact = FeedbackImpact.IMPORTANT,
        comment = "The attached screenshot shows the issue.",
        expectedResult = "Review the image with the note.",
        includeTechnicalContext = false,
        screen = "home",
        contentId = null,
        contentTitle = null,
        createdAtEpochMillis = 1_784_800_000_000,
        timezoneId = "UTC",
        attachments = listOf(attachment),
    )

    @Test
    fun attachmentPolicyAllowsReviewedImagesTextAndPdfButRejectsAudioAndOversizeFiles() {
        assertTrue(isAllowedFeedbackAttachmentMimeType("image/png"))
        assertTrue(isAllowedFeedbackAttachmentMimeType("text/plain"))
        assertTrue(isAllowedFeedbackAttachmentMimeType("application/pdf"))
        assertFalse(isAllowedFeedbackAttachmentMimeType("audio/mpeg"))
        assertTrue(attachment.isWithinPolicy())
        assertFalse(attachment.copy(sizeBytes = MAX_FEEDBACK_ATTACHMENT_BYTES + 1).isWithinPolicy())
    }

    @Test
    fun codecRoundTripsAttachmentMetadataAndRetainsLegacyNotesWithoutAttachments() {
        assertEquals(listOf(note), FeedbackCodec.decode(FeedbackCodec.encode(listOf(note))))

        val legacyFields = listOf(
            "KP-NOTE-LEGACY", "BUG", "MINOR", "old note", "", "false", "home", "", "", "100", "UTC", "2",
        ).joinToString(".") { java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(it.toByteArray()) }
        val decodedLegacy = FeedbackCodec.decode(legacyFields, legacyVersionName = "0.3.0-beta1", legacyVersionCode = 3)
        assertEquals(1, decodedLegacy.size)
        assertTrue(decodedLegacy.single().attachments.isEmpty())
    }

    @Test
    fun attachmentEmailUsesSendWithReadGrantedStreamsAndListsMetadata() {
        val controller = Robolectric.buildActivity(Activity::class.java).setup()
        try {
            val activity = controller.get()
            assertTrue(handOffFeedbackEmail(activity, listOf(note), "KP-BATCH-ATTACHMENT"))
            val launched = shadowOf(activity).nextStartedActivity
            assertEquals(Intent.ACTION_SEND, launched.action)
            assertEquals("text/plain", launched.type)
            assertEquals(Intent.FLAG_GRANT_READ_URI_PERMISSION, launched.flags and Intent.FLAG_GRANT_READ_URI_PERMISSION)
            assertEquals(Uri.parse(attachment.uri), launched.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)?.single())
            assertTrue(launched.getStringExtra(Intent.EXTRA_TEXT).orEmpty().contains(attachment.displayName))
        } finally {
            controller.pause().stop().destroy()
        }
    }

    @Test
    fun applicationContextAttachmentHandoffRetainsNewTaskFlag() {
        assertTrue(handOffFeedbackEmail(RuntimeEnvironment.getApplication(), listOf(note), "KP-BATCH-APP-ATTACHMENT"))
        val launched = shadowOf(RuntimeEnvironment.getApplication()).nextStartedActivity
        assertEquals(Intent.FLAG_ACTIVITY_NEW_TASK, launched.flags and Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}
