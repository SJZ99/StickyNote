package com.yuyue.compose.viewModel

import android.util.Log
import com.yuyue.compose.objectModel.Color
import com.yuyue.compose.objectModel.Note
import com.yuyue.compose.objectModel.Point
import com.yuyue.compose.repository.InMemoryNoteRepository
import com.yuyue.compose.repository.NoteRepository

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.*

class EditorViewModel(
    private val noteRepository: NoteRepository
) {

    private val disposableBag = CompositeDisposable()
    val allNotes = noteRepository.getAll()

    private val selectedNoteIdSubject = BehaviorSubject.create<Optional<String>>()
    val selectedNoteId: Observable<Optional<String>> = selectedNoteIdSubject.hide()

    private val selectedNoteSubject = BehaviorSubject.create<Optional<Note>>()

        init {
            val disposable = BehaviorSubject.combineLatest(allNotes, selectedNoteIdSubject) {
                notes: List<Note>, optionalId: Optional<String> ->

                    if (optionalId.isPresent) {
                        Optional.ofNullable(
                            notes.find { optionalId.get() == it.id }
                        )
                    } else Optional.empty()

            }.subscribe { optionalNote ->
                selectedNoteSubject.onNext(optionalNote)
            }

            disposableBag.add(disposable)
        }


    fun tapCanvas() {
        selectedNoteIdSubject.onNext(Optional.empty())
    }

    fun tapNote(note: Note) {
        selectedNoteIdSubject.onNext(Optional.of(note.id))
    }

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

    fun createNewNote() {
        val id = UUID.randomUUID().toString()
        val text = "new note"
        noteRepository.create(
            Note(
                id,
                text,
                Point(0f, 0f),
                Color.Gorse
            )
        )
    }

    fun deleteNoteById(id: String) {
        noteRepository.delete(id)
    }

    fun changeColorById(id: String, color: Color) {
        val note = selectedNoteSubject.value
        if (note != null && note.isPresent) {
            noteRepository.putNote(note.map { it.copy(color = color) }.get())
        }
    }
}