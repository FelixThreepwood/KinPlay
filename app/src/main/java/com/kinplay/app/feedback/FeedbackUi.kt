package com.kinplay.app.feedback

import android.content.Context
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kinplay.app.BuildConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackOverlay(
    context: Context,
    screen: String,
    contentId: String?,
    contentTitle: String?,
    modifier: Modifier = Modifier,
) {
    val store = remember(context) { FeedbackStore(context) }
    val initialNotes = remember(store) { store.load() }
    var allNotes by remember { mutableStateOf(initialNotes) }
    var selectedNoteIds by remember { mutableStateOf(activeFeedbackNotes(initialNotes).map { it.id }.toSet()) }
    var sheetOpen by rememberSaveable { mutableStateOf(false) }
    var selectedType by rememberSaveable { mutableStateOf(FeedbackType.BUG) }
    var selectedImpact by rememberSaveable { mutableStateOf(FeedbackImpact.MINOR) }
    var comment by rememberSaveable { mutableStateOf("") }
    var expectedResult by rememberSaveable { mutableStateOf("") }
    var includeTechnicalContext by rememberSaveable { mutableStateOf(true) }
    var showExpectedResult by rememberSaveable { mutableStateOf(false) }
    var showClearConfirmation by rememberSaveable { mutableStateOf(false) }
    var showArchive by rememberSaveable { mutableStateOf(false) }
    var statusMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var editingNoteId by rememberSaveable { mutableStateOf<String?>(null) }
    var pendingHandoffNoteIds by rememberSaveable { mutableStateOf(arrayListOf<String>()) }
    var showHandoffConfirmation by rememberSaveable { mutableStateOf(false) }
    var draftScreen by rememberSaveable { mutableStateOf<String?>(null) }
    var draftContentId by rememberSaveable { mutableStateOf<String?>(null) }
    var draftContentTitle by rememberSaveable { mutableStateOf<String?>(null) }
    val commentFocusRequester = remember { FocusRequester() }
    val activeNotes = activeFeedbackNotes(allNotes)
    val archivedNotes = archivedFeedbackNotes(allNotes)
    val counts = feedbackCounts(allNotes, BuildConfig.VERSION_CODE)
    val sendableSelectedNotes = selectFeedbackNotesForImmediateSend(
        notes = allNotes,
        selectedNoteIds = selectedNoteIds,
        justSavedNoteId = null,
    )

    fun currentCaptureContext() = FeedbackCaptureContext(screen, contentId, contentTitle)

    fun retainedDraftContext(): FeedbackCaptureContext? = draftScreen?.let {
        FeedbackCaptureContext(it, draftContentId, draftContentTitle)
    }

    fun openSheet() {
        if (editingNoteId == null && draftScreen == null) {
            val capture = currentCaptureContext()
            draftScreen = capture.screen
            draftContentId = capture.contentId
            draftContentTitle = capture.contentTitle
        }
        sheetOpen = true
    }

    fun persist(notes: List<FeedbackNote>) {
        allNotes = notes
        val existingIds = activeFeedbackNotes(notes).map { it.id }.toSet()
        selectedNoteIds = selectedNoteIds.intersect(existingIds)
        store.save(notes)
    }

    fun selectedNotes(): List<FeedbackNote> = selectFeedbackNotesForImmediateSend(
        notes = allNotes,
        selectedNoteIds = selectedNoteIds,
        justSavedNoteId = null,
    )

    fun launchHandoff(notesToSend: List<FeedbackNote>): Boolean {
        val opened = handOffFeedbackEmail(context, notesToSend, newFeedbackBatchId())
        if (opened) {
            pendingHandoffNoteIds = ArrayList(notesToSend.map { it.id })
            showHandoffConfirmation = true
        }
        return opened
    }

    fun clearForm() {
        comment = ""
        expectedResult = ""
        showExpectedResult = false
        draftScreen = null
        draftContentId = null
        draftContentTitle = null
    }

    fun dismissSheet() {
        if (editingNoteId != null) clearForm()
        editingNoteId = null
        sheetOpen = false
    }

    fun saveCurrentNote(): List<FeedbackNote>? {
        val original = editingNoteId?.let { id -> editableFeedbackNote(allNotes, id) }
        if (editingNoteId != null && original == null) {
            statusMessage = "The note being edited is no longer pending."
            clearForm()
            editingNoteId = null
            return null
        }
        val note = if (original != null) {
            editFeedbackNote(
                original = original,
                type = selectedType,
                impact = selectedImpact,
                comment = comment,
                expectedResult = expectedResult,
                includeTechnicalContext = includeTechnicalContext,
            )
        } else {
            val capture = resolveFeedbackCaptureContext(retainedDraftContext(), currentCaptureContext())
            createFeedbackNote(
                type = selectedType,
                impact = selectedImpact,
                comment = comment,
                expectedResult = expectedResult,
                includeTechnicalContext = includeTechnicalContext,
                screen = capture.screen,
                contentId = capture.contentId,
                contentTitle = capture.contentTitle,
            )
        }
        if (!note.isValid()) {
            statusMessage = "Add a short comment first."
            return null
        }
        val updated = if (original != null) replaceFeedbackNote(allNotes, note) else allNotes + note
        persist(updated)
        selectedNoteIds = selectedNoteIds + note.id
        clearForm()
        editingNoteId = null
        statusMessage = feedbackSaveStatusMessage(updated, wasEdit = original != null)
        return updated
    }

    fun confirmPendingHandoff() {
        val result = confirmFeedbackHandoff(
            notes = allNotes,
            pendingNoteIds = pendingHandoffNoteIds.toSet(),
            selectedNoteIds = selectedNoteIds,
            editingNoteId = editingNoteId,
            handedOffAtEpochMillis = System.currentTimeMillis(),
        )
        allNotes = result.notes
        selectedNoteIds = result.selectedNoteIds
        if (result.clearComposeForm) clearForm()
        editingNoteId = result.editingNoteId
        store.save(result.notes)
        val handedOffCount = pendingHandoffNoteIds.count { pendingId ->
            result.notes.any { it.id == pendingId && it.lifecycleState == FeedbackLifecycleState.HANDED_OFF }
        }
        pendingHandoffNoteIds = arrayListOf()
        showHandoffConfirmation = false
        statusMessage = "$handedOffCount sent note(s) moved to the archive."
    }

    fun keepPendingHandoffUnsent() {
        pendingHandoffNoteIds = arrayListOf()
        showHandoffConfirmation = false
        statusMessage = "Handoff not confirmed. Notes remain unsent and selected."
    }

    Box(modifier = modifier.fillMaxSize()) {
        ExtendedFloatingActionButton(
            onClick = ::openSheet,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .navigationBarsPadding()
                .padding(start = 16.dp, bottom = 16.dp)
                .testTag("feedback-control"),
            containerColor = Color(0xFFE3A62F),
            contentColor = Color(0xFF193A2C),
            text = {
                Text(
                    if (counts.unsent == 0) "Feedback" else "Feedback (${counts.unsent})",
                    fontWeight = FontWeight.Bold,
                )
            },
            icon = { Text("✎", fontWeight = FontWeight.Bold) },
        )
    }

    if (showHandoffConfirmation && pendingHandoffNoteIds.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = ::keepPendingHandoffUnsent,
            title = { Text("Was the feedback sent?") },
            text = {
                Text(
                    "The email app cannot report whether Send was tapped. " +
                        "Confirm only if you sent ${pendingHandoffNoteIds.size} note(s).",
                )
            },
            confirmButton = {
                Button(onClick = ::confirmPendingHandoff) { Text("Yes, mark sent") }
            },
            dismissButton = {
                OutlinedButton(onClick = ::keepPendingHandoffUnsent) { Text("No, keep unsent") }
            },
        )
    }

    if (sheetOpen) {
        LaunchedEffect(Unit) { commentFocusRequester.requestFocus() }
        ModalBottomSheet(
            onDismissRequest = ::dismissSheet,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.92f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text("Beta feedback", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Saved on this device first. Your email app opens only when you choose to send.")
                Text(
                    "Unsent: ${counts.unsent}  •  Created this app revision: ${counts.sinceRevision}",
                    fontWeight = FontWeight.Bold,
                )
                Text("Type", fontWeight = FontWeight.Bold)
                ChoiceRow {
                    FeedbackType.entries.forEach { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { Text(type.label) },
                        )
                    }
                }
                Text("Impact", fontWeight = FontWeight.Bold)
                ChoiceRow {
                    FeedbackImpact.entries.forEach { impact ->
                        FilterChip(
                            selected = selectedImpact == impact,
                            onClick = { selectedImpact = impact },
                            label = { Text(impact.label) },
                        )
                    }
                }
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it.take(2_000) },
                    label = { Text("Quick comment") },
                    supportingText = { Text("Current screen: $screen") },
                    modifier = Modifier.fillMaxWidth().focusRequester(commentFocusRequester),
                    minLines = 3,
                )
                if (showExpectedResult) {
                    OutlinedTextField(
                        value = expectedResult,
                        onValueChange = { expectedResult = it.take(1_000) },
                        label = { Text("Expected result (optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                    )
                } else {
                    TextButton(onClick = { showExpectedResult = true }) { Text("+ Add expected result") }
                }
                FilterChip(
                    selected = includeTechnicalContext,
                    onClick = { includeTechnicalContext = !includeTechnicalContext },
                    label = { Text("Include technical context") },
                )
                Text(
                    "Privacy: do not include child names, photos, audio, exact birthdates, or private family details.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            if (saveCurrentNote() != null) sheetOpen = false
                        },
                        enabled = comment.isNotBlank(),
                    ) { Text(if (editingNoteId == null) "Save note" else "Save changes") }
                    if (editingNoteId != null) {
                        OutlinedButton(
                            onClick = {
                                clearForm()
                                editingNoteId = null
                                statusMessage = "Edit canceled. Original note preserved."
                            },
                        ) { Text("Cancel edit") }
                    }
                    OutlinedButton(
                        onClick = {
                            val editingIdBeforeSave = editingNoteId
                            val idsBeforeSave = allNotes.map { it.id }.toSet()
                            val hasCurrentForm = editingIdBeforeSave != null || comment.isNotBlank()
                            val updatedNotes = if (hasCurrentForm) saveCurrentNote() ?: return@OutlinedButton else allNotes
                            val justSavedNoteId = if (hasCurrentForm) {
                                editingIdBeforeSave ?: updatedNotes.firstOrNull { it.id !in idsBeforeSave }?.id
                            } else {
                                null
                            }
                            val notesToSend = selectFeedbackNotesForImmediateSend(
                                notes = updatedNotes,
                                selectedNoteIds = selectedNoteIds,
                                justSavedNoteId = justSavedNoteId,
                            )
                            if (notesToSend.isEmpty()) {
                                statusMessage = "Select at least one note to send."
                            } else {
                                val opened = launchHandoff(notesToSend)
                                statusMessage = if (opened) {
                                    "Email app opened. Confirm here after sending; notes remain unsent until then."
                                } else {
                                    "No email app opened. Use Copy batch below; notes are still saved."
                                }
                            }
                        },
                        enabled = comment.isNotBlank() || sendableSelectedNotes.isNotEmpty(),
                    ) { Text("Send now") }
                }
                statusMessage?.let { Text(it, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold) }

                HorizontalDivider()
                Text(
                    "Unsent feedback (${activeNotes.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                if (activeNotes.isEmpty()) {
                    Text("No unsent notes.")
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = { selectedNoteIds = activeNotes.map { it.id }.toSet() }) { Text("Select all") }
                        TextButton(onClick = { selectedNoteIds = emptySet() }) { Text("Select none") }
                    }
                    activeNotes.forEach { note ->
                        PendingFeedbackCard(
                            note = note,
                            selected = note.id in selectedNoteIds,
                            onSelectedChange = { selected ->
                                selectedNoteIds = if (selected) selectedNoteIds + note.id else selectedNoteIds - note.id
                            },
                            onEdit = {
                                draftScreen = null
                                draftContentId = null
                                draftContentTitle = null
                                selectedType = note.type
                                selectedImpact = note.impact
                                comment = note.comment
                                expectedResult = note.expectedResult
                                includeTechnicalContext = note.includeTechnicalContext
                                showExpectedResult = note.expectedResult.isNotBlank()
                                editingNoteId = note.id
                                statusMessage = "Loaded for editing."
                            },
                            onDelete = {
                                persist(allNotes.filterNot { it.id == note.id })
                                statusMessage = "Deleted ${note.id}."
                            },
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                val notesToSend = selectedNotes()
                                val opened = launchHandoff(notesToSend)
                                statusMessage = if (opened) {
                                    "Email app opened. Confirm here after sending; selected notes remain unsent until then."
                                } else {
                                    "No email app opened. Copy the selected notes instead."
                                }
                            },
                            enabled = sendableSelectedNotes.isNotEmpty(),
                        ) { Text("Send selected (${sendableSelectedNotes.size})") }
                        OutlinedButton(
                            onClick = {
                                copyFeedbackBatch(context, selectedNotes(), newFeedbackBatchId())
                                statusMessage = "Selected notes copied as a formatted batch; they remain unsent."
                            },
                            enabled = sendableSelectedNotes.isNotEmpty(),
                        ) { Text("Copy selected") }
                    }
                    if (showClearConfirmation) {
                        Text("Delete every unsent note from this device? Archived notes will remain.", fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = {
                                persist(archivedNotes)
                                showClearConfirmation = false
                                statusMessage = "All unsent notes deleted."
                            }) { Text("Yes, delete unsent") }
                            OutlinedButton(onClick = { showClearConfirmation = false }) { Text("Cancel") }
                        }
                    } else {
                        TextButton(onClick = { showClearConfirmation = true }) { Text("Delete all unsent notes") }
                    }
                }

                HorizontalDivider()
                OutlinedButton(
                    onClick = { showArchive = !showArchive },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(if (showArchive) "Hide archive (${archivedNotes.size})" else "View archive (${archivedNotes.size})")
                }
                if (showArchive) {
                    if (archivedNotes.isEmpty()) {
                        Text("No archived notes yet.")
                    } else {
                        Text(
                            "Archived notes are excluded from email batches.",
                            style = MaterialTheme.typography.bodySmall,
                        )
                        archivedNotes.forEach { note ->
                            ArchivedFeedbackCard(
                                note = note,
                                onAddressed = {
                                    persist(
                                        replaceFeedbackNote(
                                            allNotes,
                                            markFeedbackNoteAddressed(
                                                note = note,
                                                addressedAtEpochMillis = System.currentTimeMillis(),
                                                addressedInVersionName = BuildConfig.VERSION_NAME,
                                                addressedInVersionCode = BuildConfig.VERSION_CODE,
                                            ),
                                        ),
                                    )
                                    statusMessage = "Marked ${note.id} addressed in ${BuildConfig.VERSION_NAME}."
                                },
                                onCompleted = {
                                    persist(
                                        replaceFeedbackNote(
                                            allNotes,
                                            markFeedbackNoteCompleted(note, System.currentTimeMillis()),
                                        ),
                                    )
                                    statusMessage = "Marked ${note.id} completed."
                                },
                            )
                        }
                    }
                }
                OutlinedButton(onClick = ::dismissSheet, modifier = Modifier.fillMaxWidth()) { Text("Return to KinPlay") }
            }
        }
    }
}

@Composable
private fun ChoiceRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = content,
    )
}

@Composable
private fun PendingFeedbackCard(
    note: FeedbackNote,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = selected, onCheckedChange = onSelectedChange)
                Text("${note.type.label} • ${note.impact.label}", fontWeight = FontWeight.Bold)
            }
            Text(note.comment)
            Text(note.contentTitle ?: note.screen, style = MaterialTheme.typography.bodySmall)
            Text(formatFeedbackCreationMetadata(note), style = MaterialTheme.typography.bodySmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onEdit) { Text("Edit") }
                TextButton(onClick = onDelete) { Text("Delete") }
            }
        }
    }
}

@Composable
private fun ArchivedFeedbackCard(
    note: FeedbackNote,
    onAddressed: () -> Unit,
    onCompleted: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                "${note.type.label} • ${note.impact.label} • ${note.lifecycleState.label}",
                fontWeight = FontWeight.Bold,
            )
            Text(note.comment)
            Text(note.contentTitle ?: note.screen, style = MaterialTheme.typography.bodySmall)
            Text(formatFeedbackCreationMetadata(note), style = MaterialTheme.typography.bodySmall)
            Text(formatFeedbackResolutionMetadata(note), style = MaterialTheme.typography.bodySmall)
            when (note.lifecycleState) {
                FeedbackLifecycleState.HANDED_OFF -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = onAddressed) { Text("Mark addressed") }
                        TextButton(onClick = onCompleted) { Text("Mark completed") }
                    }
                }
                FeedbackLifecycleState.ADDRESSED -> {
                    TextButton(onClick = onCompleted) { Text("Mark completed") }
                }
                FeedbackLifecycleState.UNSENT,
                FeedbackLifecycleState.COMPLETED,
                -> Unit
            }
        }
    }
}
