package com.yuyue.compose.repository

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.yuyue.compose.objectModel.Color
import com.yuyue.compose.objectModel.Note
import com.yuyue.compose.objectModel.Point
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.*
import java.util.concurrent.TimeUnit

class FirebaseNoteRepository : NoteRepository {
    private val firebase = FirebaseFirestore.getInstance()
    private val query = firebase.collection(COLLECTION_PATH).limit(20)

    private val firebaseNoteSubject = BehaviorSubject.createDefault<List<Note>>(emptyList())
    private val localNoteSubject
                    = BehaviorSubject.createDefault<Optional<Note>>(Optional.empty())

    companion object {
        const val COLLECTION_PATH = "notes"
        const val FIELD_TEXT = "text"
        const val FIELD_POSITION_X = "positionX"
        const val FIELD_POSITION_Y = "positionY"
        const val FIELD_COLOR = "color"

        private fun documentToNote(document: DocumentSnapshot): Note {
            val data: Map<String, Any> = document.data ?: emptyMap()
            val text = data[FIELD_TEXT] as? String ?: ""
            val positionX = data[FIELD_POSITION_X] as? String ?: "0"
            val positionY = data[FIELD_POSITION_Y] as? String ?: "0"
            val color = data[FIELD_COLOR] as? Long ?: 0xFFFEFF9C

            return Note(
                id = document.id,
                text = text,
                position = Point(positionX.toFloat(), positionY.toFloat()),
                color = Color(color)
            )
        }
    }

    init {

        val onSnapshotUpdated = { result: QuerySnapshot ->
            val allNotes = result.documents.map {
                documentToNote(it)
            }
            firebaseNoteSubject.onNext(allNotes)
        }

        // when firebase update, will refresh the list of notes
        query.addSnapshotListener { result, error ->
            result?.let {
                onSnapshotUpdated(it)
            }
        }

        // push new note to firebase
        localNoteSubject
            .throttleWithTimeout(200, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.io())
            .subscribe { optional ->
                optional.ifPresent {
                    setNoteDocument(it)
                }
            }

        // clean too old note in local history
        localNoteSubject
            .filter { it.isPresent }
            .debounce(600, TimeUnit.MILLISECONDS)
            .subscribe {
                localNoteSubject.onNext(Optional.empty())
            }
    }

    private fun setNoteDocument(note: Note) {
        val noteData = hashMapOf(
            FIELD_TEXT to note.text,
            FIELD_POSITION_X to note.position.x.toString(),
            FIELD_POSITION_Y to note.position.y.toString(),
            FIELD_COLOR to note.color.color
        )

        firebase.collection(COLLECTION_PATH)
                .document(note.id)
                .set(noteData)
    }

    override fun getAll(): Observable<List<Note>> {
        return Observable.combineLatest(firebaseNoteSubject, localNoteSubject) {
            allWebNote: List<Note>, localNewNote: Optional<Note>
            ->
            var result = allWebNote
            localNewNote.ifPresent { newNote ->
                val index = allWebNote.indexOfFirst { it.id == newNote.id }
                result = allWebNote.subList(0, index) + newNote + allWebNote.subList(index + 1, allWebNote.size)
            }
            result
        }
    }

    // create -C
    override fun create(note: Note) {
        setNoteDocument(note)
    }

    // read -R
    override fun getNoteById(id: String): Observable<Note> {
        return firebaseNoteSubject.map { notes ->
            Optional.ofNullable(notes.find { it.id == id })
        }.mapOptional { it }
    }

    // update -U
    override fun putNote(note: Note) {
        localNoteSubject.onNext(Optional.of(note))
    }

    // delete -D
    override fun delete(id: String) {
        firebase.collection(COLLECTION_PATH)
                .document(id)
                .delete()
    }
}