package com.yuyue.compose.viewModel

import android.util.Log
import com.yuyue.compose.repository.InMemoryNoteRepository
import com.yuyue.compose.repository.NoteRepository
import com.yuyue.compose.view.Note
import com.yuyue.compose.view.Point
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.*

class EditorViewModel(
    private val noteRepository: NoteRepository
) {

    private val disposableBag = CompositeDisposable()
    val allNotes = noteRepository.getAll()

    fun moveNote(noteId: String, move: Point) {
        val disposable =
            Observable.just(move)
            .withLatestFrom(allNotes) {
                point: Point, notes: List<Note>
                ->
                val note = notes.find { it.id == noteId }
                val newNote = note?.copy(position = (note.position + point))
                Optional.ofNullable(newNote)
            }
            .subscribe {
                optional ->
                    optional.ifPresent {
                        noteRepository.putNote(it)
                    }
            }
        disposableBag.add(disposable)
    }
}