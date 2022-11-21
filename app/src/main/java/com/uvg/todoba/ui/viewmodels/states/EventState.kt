package com.uvg.todoba.ui.viewmodels.states

import com.uvg.todoba.data.local.entity.Event

sealed interface EventState{
    data class Updated(val events: List<Event>): EventState
    data class Fetched(val event: Event): EventState
    data class Error(val message: String): EventState
    object Empty: EventState
    object Loading: EventState
}
