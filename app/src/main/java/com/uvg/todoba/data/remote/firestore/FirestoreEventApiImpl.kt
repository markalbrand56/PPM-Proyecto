package com.uvg.todoba.data.remote.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.uvg.todoba.data.Resource
import com.uvg.todoba.data.remote.dto.EventDTO
import com.uvg.todoba.data.remote.api.EventsApi
import kotlinx.coroutines.tasks.await

class FirestoreEventApiImpl(
    private val firestore: FirebaseFirestore
): EventsApi {
    override suspend fun insert(event: EventDTO, userId: String):Resource<Boolean> {
        return try {
            firestore.collection(userId)
                .document("data")
                .collection("events")
                .document("${event.firestoreId}")
                .set(event)
                .await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error")
        }
    }

    override suspend fun update(event: EventDTO, userId: String): Resource<Boolean> {
        return try {
            firestore.collection(userId)
                .document("data")
                .collection("events")
                .document("${event.firestoreId}")
                .set(event)
                .await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error")
        }
    }

    override suspend fun getById(id: String, userId: String): Resource<EventDTO?> {
        return try {
            val document = firestore.collection(userId)
                .document("data")
                .collection("events")
                .document("$id")
                .get()
                .await()
            Resource.Success(document.toObject<EventDTO>())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error")
        }

    }

    override suspend fun getAll(userId: String): Resource<List<EventDTO>?> {
        return try {
            val querySnapshot = firestore.collection("/${userId}/data/events")
                .get()
                .await()
            val result = querySnapshot.documents.map {documentSnapshot -> documentSnapshot.toObject<EventDTO>()!!}
            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error")
        }
    }

    override suspend fun deleteById(id: String, userId: String): Resource<Boolean> {
        return try {
            firestore.collection(userId)
                .document("data")
                .collection("events")
                .document("$id")
                .delete()
                .await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error")
        }
    }

    override suspend fun deleteAll(userId: String): Resource<Boolean> {
        return try {
            firestore.collection(userId)
                .document("data")
                .collection("events")
                .get()
                .await()
                .documents
                .forEach { documentSnapshot ->
                    documentSnapshot.reference.delete()
                }
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error")
        }
    }


}