package com.yuyue.compose.view


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yuyue.compose.R
import com.yuyue.compose.viewModel.EditorViewModel

const val ICON_SIZE = 27

@Composable
fun OperationMenuView(
    delete: () -> Unit,
    changeColor: (com.yuyue.compose.objectModel.Color) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        elevation = 3.dp,
    ) {
        var colorExpended by remember {
            mutableStateOf(false)
        }

        Row {
            IconButton(onClick = { delete() }) {
                val painter = painterResource(id = R.drawable.delete)
                Icon(
                    painter = painter, contentDescription = "delete",
                    modifier = Modifier.size(ICON_SIZE.dp),
                    tint = Color.Unspecified
                )
            }

            IconButton(onClick = {}) {
                val painter = painterResource(id = R.drawable.text)
                Icon(
                    painter = painter, contentDescription = "text",
                    modifier = Modifier.size(ICON_SIZE.dp),
                    tint = Color.Unspecified
                )
            }

            IconButton(onClick = { colorExpended = true }) {
                val painter = painterResource(id = R.drawable.color)
                Icon(
                    painter = painter, contentDescription = "color",
                    modifier = Modifier.size(ICON_SIZE.dp),
                    tint = Color.Unspecified
                )
                
                DropdownMenu(expanded = colorExpended, onDismissRequest = { colorExpended = false }) {
                    for (color in com.yuyue.compose.objectModel.Color.defaultColors) {
                        DropdownMenuItem(onClick = { changeColor(color) }) {
                            Box(
                                modifier = Modifier.size(ICON_SIZE.dp)
                                    .background(Color(color.color), CircleShape)
                            )
                        }
                    }
                }
            }
        }
    }
}