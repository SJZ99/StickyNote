package com.yuyue.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yuyue.compose.repository.FirebaseNoteRepository
import com.yuyue.compose.ui.theme.ComposeTheme
import com.yuyue.compose.view.EditorView
import com.yuyue.compose.viewModel.EditorViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = FirebaseNoteRepository()
        val editorViewModel = EditorViewModel(repository)

        setContent {
            ComposeTheme(false) {
                EditorView(model = editorViewModel)
            }
        }
    }
}
