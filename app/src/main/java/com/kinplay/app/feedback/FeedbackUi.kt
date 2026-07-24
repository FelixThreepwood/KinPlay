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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

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
    var pendingNotes by remember { mutableStateOf(initialNotes) }
    var selectedNoteIds by remember { mutableStateOf(initialNotes.map { it.id }.toSet()) }
    var sheetOpen by rememberSaveable { mutableStateOf(false) }
    var selectedType by rememberSaveable { mutableStateOf(FeedbackType.BUG) }
    var selectedImpact by rememberSaveable { mutableStateOf(FeedbackImpact.MINOR) }
    var comment by rememberSaveable { mutableStateOf("") }
    var expectedResult by rememberSaveable { mutableStateOf("") }
    var includeTechnicalContext by rememberSaveable { mutableStateOf(true) }
    var showExpectedResult by rememberSaveable { mutableStateOf(false) }
    var showClearConfirmation by rememberSaveable { mutableStateOf(false) }
    var statusMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var editingNoteId by rememberSaveable { mutableStateOf<String?>(null) }
    var draftScreen by rememberSaveable { mutableStateOf<String?>(null) }
    var draftContentId by rememberSaveable { mutableStateOf<String?>(null) }
    var draftContentTitle by rememberSaveable { mutableStateOf<String?>(null) }
    val commentFocusRequester = remember { FocusRequester() }

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
        pendingNotes = notes
        val existingIds = notes.map { it.id }.toSet()
        selectedNoteIds = selectedNoteIds.intersect(existingIds)
        store.save(notes)
    }

    fun selectedNotes(): List<FeedbackNote> = pendingNotes.filter { it.id in selectedNoteIds }

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
        val original = editingNoteId?.let { id -> pendingNotes.firstOrNull { it.id == id } }
        if (editingNoteId != null && original == null) {
            statusMessage = "The note being edited is no longer pending."
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
        val updated = if (original != null) replaceFeedbackNote(pendingNotes, note) else pendingNotes + note
        persist(updated)
        selectedNoteIds = selectedNoteIds + note.id
        clearForm()
        editingNoteId = null
        statusMessage = if (original != null) {
            "Updated locally. ${updated.size} pending."
        } else {
            "Saved locally. ${updated.size} pending."
        }
        return updated
    }

    Box(modifier = modifier.fillMaxSize()) {
        ExtendedFloatingActionButton(
            onClick = ::openSheet,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .navigationBarsPadding()
                .padding(start = 16.dp, bottom = 16.dp),
            containerColor = Color(0xFFE3A62F),
            contentColor = Color(0xFF193A2C),
            text = {
                Text(
                    if (pendingNotes.isEmpty()) "Feedback" else "Feedback (${pendingNotes.size})",
                    fontWeight = FontWeight.Bold,
                )
            },
            icon = { Text("✎", fontWeight = FontWeight.Bold) },
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
                            val idsBeforeSave = pendingNotes.map { it.id }.toSet()
                            val hasCurrentForm = editingIdBeforeSave != null || comment.isNotBlank()
                            val updatedNotes = if (hasCurrentForm) saveCurrentNote() ?: return@OutlinedButton else pendingNotes
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
                                val opened = handOffFeedbackEmail(context, notesToSend, newFeedbackBatchId())
                                statusMessage = if (opened) {
                                    "Email handoff opened. Notes remain pending until you delete them."
                                } else {
                                    "No email app opened. Use Copy batch below; notes are still saved."
                                }
                            }
                        },
                        enabled = comment.isNotBlank() || selectedNoteIds.isNotEmpty(),
                    ) { Text("Send now") }
                }
                statusMessage?.let { Text(it, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold) }

                HorizontalDivider()
                Text("Pending feedback (${pendingNotes.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                if (pendingNotes.isEmpty()) {
                    Text("No saved notes yet.")
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = { selectedNoteIds = pendingNotes.map { it.id }.toSet() }) { Text("Select all") }
                        TextButton(onClick = { selectedNoteIds = emptySet() }) { Text("Select none") }
                    }
                    pendingNotes.forEach { note ->
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
                                persist(pendingNotes.filterNot { it.id == note.id })
                                statusMessage = "Deleted ${note.id}."
                            },
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                val opened = handOffFeedbackEmail(context, selectedNotes(), newFeedbackBatchId())
                                statusMessage = if (opened) {
                                    "Email handoff opened. Review and tap Send there."
                                } else {
                                    "No email app opened. Copy the selected notes instead."
                                }
                            },
                            enabled = selectedNoteIds.isNotEmpty(),
                        ) { Text("Send selected (${selectedNoteIds.size})") }
                        OutlinedButton(
                            onClick = {
                                copyFeedbackBatch(context, selectedNotes(), newFeedbackBatchId())
                                statusMessage = "Selected notes copied as a formatted batch."
                            },
                            enabled = selectedNoteIds.isNotEmpty(),
                        ) { Text("Copy selected") }
                    }
                    if (showClearConfirmation) {
                        Text("Delete every pending note from this device?", fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = {
                                persist(emptyList())
                                showClearConfirmation = false
                                statusMessage = "All pending notes deleted."
                            }) { Text("Yes, delete all") }
                            OutlinedButton(onClick = { showClearConfirmation = false }) { Text("Cancel") }
                        }
                    } else {
                        TextButton(onClick = { showClearConfirmation = true }) { Text("Delete all pending notes") }
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
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onEdit) { Text("Edit") }
                TextButton(onClick = onDelete) { Text("Delete") }
            }
        }
    }
}
