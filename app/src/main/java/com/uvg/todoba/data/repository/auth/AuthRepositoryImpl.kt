package com.uvg.todoba.data.repository.auth

import com.uvg.todoba.data.Resource
import com.uvg.todoba.data.remote.api.AuthAPI

class AuthRepositoryImpl(
    private val authAPI: AuthAPI
) : AuthRepository {
    override suspend fun signInWithEmailAndPassword(email: String, password: String): String? {
        return when (val response = authAPI.signInWithEmailAndPassword(email, password)) {
            is Resource.Success -> response.data!!
            is Resource.Error -> null
        }
    }

    override suspend fun createAccountWithEmailAndPassword(
        email: String,
        password: String
    ): String? {
        return when (val response = authAPI.createAccountWithEmailAndPassword(email, password)) {
            is Resource.Success -> response.data!!
            is Resource.Error -> null
        }
    }
}
