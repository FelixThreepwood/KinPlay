package com.kinplay.app.feedback

import android.app.Activity
import android.content.ContextWrapper
import android.content.Intent
import android.view.ContextThemeWrapper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [35])
class FeedbackEmailHandoffTest {
    private val unsentNote = FeedbackNote(
        id = "KP-NOTE-UNSENT",
        type = FeedbackType.BUG,
        impact = FeedbackImpact.IMPORTANT,
        comment = "Include this unsent note.",
        expectedResult = "Open the email client.",
        includeTechnicalContext = false,
        screen = "home",
        contentId = null,
        contentTitle = null,
        createdAtEpochMillis = 1_784_800_000_000,
        timezoneId = "UTC",
    )

    @Test
    fun wrappedActivityLaunchesProductionEmailIntentWithoutNewTask() {
        val controller = Robolectric.buildActivity(Activity::class.java).setup()
        try {
            val activity = controller.get()
            val wrappedActivity = ContextThemeWrapper(ContextWrapper(activity), android.R.style.Theme_Material)

            assertTrue(handOffFeedbackEmail(wrappedActivity, listOf(unsentNote), "KP-BATCH-WRAPPED"))

            val launchedIntent = shadowOf(activity).nextStartedActivity
            assertNotNull(launchedIntent)
            assertEquals(Intent.ACTION_SENDTO, launchedIntent.action)
            assertEquals(0, launchedIntent.flags and Intent.FLAG_ACTIVITY_NEW_TASK)
            assertEquals("mailto", launchedIntent.data?.scheme)
            assertEquals(FEEDBACK_RECIPIENT, launchedIntent.data?.schemeSpecificPart?.substringBefore('?'))
        } finally {
            controller.pause().stop().destroy()
        }
    }

    @Test
    fun applicationContextLaunchesProductionEmailIntentInNewTaskWithOnlyUnsentNotes() {
        val application = RuntimeEnvironment.getApplication()
        val handedOffNote = unsentNote.copy(
            id = "KP-NOTE-HANDED-OFF",
            comment = "Do not include this handed-off note.",
            lifecycleState = FeedbackLifecycleState.HANDED_OFF,
            handedOffAtEpochMillis = 1_784_800_000_100,
        )

        assertTrue(
            handOffFeedbackEmail(
                application,
                listOf(unsentNote, handedOffNote),
                "KP-BATCH-APPLICATION",
            ),
        )

        val launchedIntent = shadowOf(application).nextStartedActivity
        assertNotNull(launchedIntent)
        assertEquals(Intent.ACTION_SENDTO, launchedIntent.action)
        assertEquals(Intent.FLAG_ACTIVITY_NEW_TASK, launchedIntent.flags and Intent.FLAG_ACTIVITY_NEW_TASK)
        val body = decodeMailtoBody(launchedIntent)
        assertTrue(body.contains(unsentNote.comment))
        assertFalse(body.contains(handedOffNote.comment))
    }

    @Test
    fun runtimeFailureFromProductionContextLaunchReturnsFalse() {
        val throwingContext = object : ContextWrapper(RuntimeEnvironment.getApplication()) {
            override fun startActivity(intent: Intent) {
                throw IllegalStateException("external activity launch failed")
            }
        }

        assertFalse(handOffFeedbackEmail(throwingContext, listOf(unsentNote), "KP-BATCH-FAILURE"))
    }

    @Test
    fun cyclicContextWrapperChainTerminatesAndUsesNewTask() {
        val cyclicContext = object : ContextWrapper(null) {
            lateinit var launchedIntent: Intent

            init {
                attachBaseContext(this)
            }

            override fun startActivity(intent: Intent) {
                launchedIntent = intent
            }
        }

        assertTrue(handOffFeedbackEmail(cyclicContext, listOf(unsentNote), "KP-BATCH-CYCLE"))
        assertEquals(
            Intent.FLAG_ACTIVITY_NEW_TASK,
            cyclicContext.launchedIntent.flags and Intent.FLAG_ACTIVITY_NEW_TASK,
        )
    }

    private fun decodeMailtoBody(intent: Intent): String {
        val encodedBody = intent.data.toString().substringAfter("&body=")
        return URLDecoder.decode(encodedBody, StandardCharsets.UTF_8.name())
    }
}