package com.uvg.todoba.data.remote.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.uvg.todoba.data.Resource
import com.uvg.todoba.data.remote.api.CategoriesApi
import com.uvg.todoba.data.remote.dto.CategoryDTO
import kotlinx.coroutines.tasks.await

class FirestoreCategoryApiImpl(
    private val firestore: FirebaseFirestore,
): CategoriesApi {
    override suspend fun insert(category: CategoryDTO, userId: String): Resource<Boolean> {
        return try {
            firestore.collection(userId)
                .document("data")
                .collection("categories")
                .document("${category.firebaseId}")
                .set(category)
                .await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error")
        }
    }

    override suspend fun getAll(userId: String): List<CategoryDTO>? {
        return try {
            val querySnapshot = firestore.collection("/${userId}/data/categories")
                .get()
                .await()
            val result = querySnapshot.documents.map {documentSnapshot -> documentSnapshot.toObject<CategoryDTO>()!!}
            result
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun deleteAll(userId: String): Resource<Boolean> {
        return try {
            firestore.collection("/${userId}/data/categories")
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