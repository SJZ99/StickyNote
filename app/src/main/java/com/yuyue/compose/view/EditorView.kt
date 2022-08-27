package com.yuyue.compose.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yuyue.compose.R
import com.yuyue.compose.objectModel.Note
import com.yuyue.compose.viewModel.EditorViewModel
import java.util.*

@Composable
fun EditorView(
    model: EditorViewModel
) {

    val notes = model.allNotes.subscribeAsState(initial = emptyList())
    val onTap = { note: Note ->
        model.tapNote(note)
    }
    val selectedNote = model.selectingNote.subscribeAsState(initial = Optional.empty())

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput("editor") {
                detectTapGestures { model.tapCanvas() }
            }
    ) {
        Box {
            BoardView(
                notes = notes,
                updateNotePosition = model::moveNote,
                onTap = onTap,
                selectedNote
            )

            AnimatedVisibility(
                visible = !selectedNote.value.isPresent,
                modifier = Modifier.align(Alignment.BottomEnd)
                                    .padding(30.dp)
                                    .size((ICON_SIZE * 1.8).dp)
            ) {
                FloatingActionButton(
                    onClick = {},
                ) {
                    val painter = painterResource(id = R.drawable.plus)
                    Icon(
                        painter = painter,
                        contentDescription = "add note",
                        tint = Color.Unspecified
                    )

                }
            }

            AnimatedVisibility(
                visible = selectedNote.value.isPresent,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                OperationMenuView()
            }
        }
    }

}