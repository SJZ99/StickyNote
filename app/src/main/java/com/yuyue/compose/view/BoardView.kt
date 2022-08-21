package com.yuyue.compose.view

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun BoardView(
    notes: State<List<Note>>,
    updateNotePosition: (String, Point) -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {

        notes.value.forEach { note ->

            val onDrag = {
                p: Point -> updateNotePosition(note.id, p)
            }
            StickyNote(note, onDrag)
        }
    }
}