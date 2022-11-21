package com.uvg.todoba.data.repository.auth

import com.uvg.todoba.data.Resource
import com.uvg.todoba.data.remote.api.AuthAPI

class AuthRepositoryImpl(
    private val authAPI: AuthAPI
) : AuthRepository {
    override suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<String?> {
        return when (val response = authAPI.signInWithEmailAndPassword(email, password)) {
            is Resource.Success -> Resource.Success(response.data!!)
            is Resource.Error -> Resource.Error(response.message ?: "Error")
        }
    }

    override suspend fun createAccountWithEmailAndPassword(
        email: String,
        password: String
    ): Resource<String? >{
        return when (val response = authAPI.createAccountWithEmailAndPassword(email, password)) {
            is Resource.Success -> Resource.Success(response.data!!)
            is Resource.Error -> Resource.Error(response.message ?: "Error")
        }
    }
}
