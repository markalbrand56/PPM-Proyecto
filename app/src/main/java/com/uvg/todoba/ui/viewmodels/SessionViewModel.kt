package com.uvg.todoba.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.todoba.data.repository.auth.AuthRepository
import com.uvg.todoba.ui.viewmodels.states.SessionState
import com.uvg.todoba.util.dataStore
import com.uvg.todoba.util.setPreference
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SessionViewModel(
    private val authRepository: AuthRepository,
    private val context: Context
): ViewModel() {
    private val _sessionState = MutableStateFlow<SessionState>(SessionState.LoggedOut)
    val sessionState = _sessionState
    private var sessionJob: Job? = null

    fun signIn(email: String, password: String) {
        sessionJob?.cancel()
        sessionJob = viewModelScope.launch {
            _sessionState.value = SessionState.Loading
            try {
                val result = authRepository.signInWithEmailAndPassword(email, password)
                if (result is com.uvg.todoba.data.Resource.Success) {
                    context.dataStore.setPreference("user", result.data!!)
                    _sessionState.value = SessionState.LoggedIn(result.data)
                } else {
                    _sessionState.value = SessionState.Error("Error al iniciar sesión")
                }
            } catch (e: Exception) {
                _sessionState.value = SessionState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun signUp(email: String, password: String) {
        sessionJob?.cancel()
        sessionJob = viewModelScope.launch {
            _sessionState.value = SessionState.Loading
            try {
                val result = authRepository.createAccountWithEmailAndPassword(email, password)
                if (result is com.uvg.todoba.data.Resource.Success) {
                    context.dataStore.setPreference("user", result.data!!)
                    _sessionState.value = SessionState.LoggedIn(result.data!!)
                } else {
                    _sessionState.value = SessionState.Error("Error al crear la cuenta")
                }
            } catch (e: Exception) {
                _sessionState.value = SessionState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun logOut() {
        sessionJob?.cancel()
        sessionJob = viewModelScope.launch {
            try {
                context.dataStore.setPreference("user", "")
                _sessionState.value = SessionState.LoggedOut
            } catch (e: Exception) {
                _sessionState.value = SessionState.Error(e.message ?: "Unknown error")
            }
        }
    }
}