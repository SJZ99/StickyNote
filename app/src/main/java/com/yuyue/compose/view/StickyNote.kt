package com.yuyue.compose.view

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.yuyue.compose.objectModel.Note
import com.yuyue.compose.objectModel.Point
import java.util.*

private val highlightBorder: @Composable Modifier.(Boolean) -> Modifier = {
    isHighlight ->
    if (isHighlight) {
        this.border(2.dp, Color.Black, MaterialTheme.shapes.medium)
    } else {
        this
    }
}

@Composable
fun StickyNote(
    note: Note,
    onDrag: (Point) -> Unit,
    onTap: (Note) -> Unit,
    isSelected: Boolean = false
) {

    Surface(
        elevation = 4.dp,
        modifier = Modifier
            .offset(note.position.x.dp, note.position.y.dp)
            .size(Note.SIDE_LENGTH.dp)
            .zIndex(30f)
            .highlightBorder(isSelected)
            .clickable { onTap(note) }
            .pointerInput(note.id) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onDrag(Point(dragAmount.x.toDp().value, dragAmount.y.toDp().value))
                }
            },
        color = androidx.compose.ui.graphics.Color(note.color.color),
    ) {

        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = note.text)
        }
    }
//    StickyNoteDecoration(note)
}

@Composable
fun StickyNoteDecoration(note: Note) {
    val length = 45
    val offsetX = note.position.x + Note.SIDE_LENGTH - length
    val offsetY = note.position.y + Note.SIDE_LENGTH - 31

    Surface(
        elevation = 4.dp,
        modifier = Modifier
            .offset(offsetX.dp, offsetY.dp)
            .size(length.dp)
            .clip(CutCornerShape(9.dp))
            .clip(RoundedCornerShape(14.dp))
            .zIndex(0f),
        color = androidx.compose.ui.graphics.Color(note.color.color)
    ) {}
}
