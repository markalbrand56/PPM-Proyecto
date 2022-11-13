package com.uvg.todoba.data.remote.api

import com.uvg.todoba.data.Resource

interface AuthAPI {
    suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<String>
    suspend fun createAccountWithEmailAndPassword(email: String, password: String): Resource<String>
}