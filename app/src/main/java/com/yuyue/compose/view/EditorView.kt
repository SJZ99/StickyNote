package com.yuyue.compose.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rxjava3.subscribeAsState
import com.yuyue.compose.viewModel.EditorViewModel

@Composable
fun EditorView(model: EditorViewModel) {

    val notes = model.allNotes.subscribeAsState(initial = emptyList())
    
    BoardView(
        notes = notes,
        updateNotePosition = model::moveNote
    )
}