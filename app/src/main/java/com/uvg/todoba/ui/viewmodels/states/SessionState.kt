package com.uvg.todoba.ui.viewmodels.states

sealed class SessionState {
    object Loading: SessionState()
    data class LoggedIn(val uid: String): SessionState()
    object LoggedOut: SessionState()
    data class Error(val message: String): SessionState()
}