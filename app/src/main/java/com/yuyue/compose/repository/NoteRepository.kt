package com.yuyue.compose.repository

import com.yuyue.compose.objectModel.Note
import io.reactivex.rxjava3.core.Observable

interface NoteRepository {
    fun getAll(): Observable<List<Note>>
    fun putNote(note: Note)
    fun create(note: Note)
    fun getNoteById(id: String): Observable<Note>
    fun delete(id: String)
}