package com.yuyue.compose.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.yuyue.compose.objectModel.Note
import com.yuyue.compose.objectModel.Point
import java.util.*

@Composable
fun BoardView(
    notes: State<List<Note>>,
    updateNotePosition: (String, Point) -> Unit,
    onTap: (Note) -> Unit,
    selectedNote: State<Optional<Note>>
) {

    Box(modifier = Modifier.fillMaxSize()) {

        notes.value.forEach { note ->
            val isSelected = selectedNote.value.isPresent
                    && selectedNote.value.get().id == note.id

            val onDrag = {
                p: Point -> updateNotePosition(note.id, p)
            }
            StickyNote(note, onDrag, onTap, isSelected)
        }
    }
}