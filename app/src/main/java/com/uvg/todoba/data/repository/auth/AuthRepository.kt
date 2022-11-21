package com.uvg.todoba.data.repository.auth

import com.uvg.todoba.data.Resource

interface AuthRepository {
    suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<String?>
    suspend fun createAccountWithEmailAndPassword(email: String, password: String): Resource<String?>
}