package com.uvg.todoba.data.remote.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.uvg.todoba.data.remote.dto.EventDTO
import com.uvg.todoba.data.remote.api.EventsApi
import kotlinx.coroutines.tasks.await

class FirestoreEventApiImpl(
    private val db: FirebaseFirestore
): EventsApi {
    override suspend fun insert(event: EventDTO, userId: String) {
        db.collection("$/${userId}/data/events")
            .add(event)
            .await()
    }

    override suspend fun update(event: EventDTO, userId: String) {
        db.collection("/${userId}/data/events")
            .document(event.id.toString())
            .set(event)
            .await()
    }

    override suspend fun getById(id: Int, userId: String): EventDTO? {
        try {
            val document = db.collection("/${userId}/data/events")
                .document(id.toString())
                .get()
                .await()
            return document.toObject<EventDTO>()
        } catch (e: Exception) {
            return null
        }

    }

    override suspend fun getAll(userId: String): List<EventDTO>? {
        try {
            val querySnapshot = db.collection("/${userId}/data/events")
                .get()
                .await()
            println("\n\n\n\n\n\n\n\n\n\n")
            println(querySnapshot.documents)
            val result = querySnapshot.documents.map {documentSnapshot -> documentSnapshot.toObject<EventDTO>()!!}
            return result
        } catch (e: Exception) {
            println(e.message)
            return null
        }
    }

    override suspend fun deleteById(id: Int, userId: String) {
        db.collection("/${userId}/data/events")
            .document(id.toString())
            .delete()
            .await()
    }

    override suspend fun deleteAll(userId: String) {
        db.collection("/${userId}/data/events")
            .get()
            .await()
            .documents
            .forEach {
                it.reference.delete()
            }
    }


}