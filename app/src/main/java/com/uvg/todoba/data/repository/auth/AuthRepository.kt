package com.uvg.todoba.data.repository.auth

interface AuthRepository {
    suspend fun signInWithEmailAndPassword(email: String, password: String): String?
    suspend fun createAccountWithEmailAndPassword(email: String, password: String): String?
}