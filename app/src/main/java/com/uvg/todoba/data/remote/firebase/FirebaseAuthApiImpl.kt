package com.uvg.todoba.data.remote.firebase

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.uvg.todoba.data.Resource
import com.uvg.todoba.data.remote.api.AuthAPI
import kotlinx.coroutines.tasks.await

class FirebaseAuthApiImpl: AuthAPI {
    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Resource<String> {
        val auth = Firebase.auth

        val response = auth.signInWithEmailAndPassword(email, password).await()

        val userInfo = response.user
        return if (userInfo != null) {
            Resource.Success(userInfo.uid)
        } else {
            Resource.Error("Error al iniciar sesión")
        }
    }
}