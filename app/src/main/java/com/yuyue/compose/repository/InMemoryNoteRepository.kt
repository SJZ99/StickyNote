package com.yuyue.compose.repository

import android.util.Log
import com.yuyue.compose.objectModel.Note
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

import java.util.concurrent.ConcurrentHashMap

class InMemoryNoteRepository{
    private val notesMap = ConcurrentHashMap<String, Note>()
    private val notesSubject = BehaviorSubject.create<List<Note>>()

    init {
        val note = Note.generateRandomNote()
        notesMap[note.id] = note
        notesSubject.onNext(notesMap.elements().toList())
    }

    fun getAll(): Observable<List<Note>> {
        return notesSubject.hide()
    }

    fun putNote(note: Note) {
        notesMap[note.id] = note
        notesSubject.onNext(notesMap.elements().toList())
    }
}