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

        return try {
            println("Signing in with email: '$email'")
            println("Signing in with password: '$password'")
            val result = auth.signInWithEmailAndPassword(email, password).await()
            if (result.user != null) {
                Resource.Success(result.user!!.uid)
            } else {
                Resource.Error("Error al iniciar sesión")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al iniciar sesión")
        }
    }

    override suspend fun createAccountWithEmailAndPassword(
        email: String,
        password: String
    ): Resource<String> {
        val auth = Firebase.auth

        return try {
            println("Creating account with email: '$email'")
            println("Creating account with password: '$password'")
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            if (result.user != null) {
                Resource.Success(result.user!!.uid)
            } else {
                Resource.Error("Error al crear la cuenta")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al crear la cuenta")
        }
    }
}