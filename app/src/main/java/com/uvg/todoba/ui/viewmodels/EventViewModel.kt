package com.uvg.todoba.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.todoba.data.local.entity.Event
import com.uvg.todoba.data.repository.event.EventRepository
import com.uvg.todoba.ui.viewmodels.states.EventState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventViewModel(
    private val eventRepository: EventRepository
): ViewModel() {
    private val _eventState = MutableStateFlow<EventState>(EventState.Empty)
    val eventState: StateFlow<EventState> = _eventState

    private var eventJob: Job? = null

    fun getEvents(uid: String){
        eventJob?.cancel()
        eventJob = viewModelScope.launch {
            _eventState.value = EventState.Loading
            try {
                val events = eventRepository.getEvents(uid)
                if (events != null) {
                    _eventState.value = EventState.Updated(events)
                    _eventState.value = EventState.Empty
                } else {
                    _eventState.value = EventState.Empty
                }
            } catch (e: Exception){
                _eventState.value = EventState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteEvent(uid: String, event: Event){
        eventJob?.cancel()
        eventJob = viewModelScope.launch {
            _eventState.value = EventState.Loading
            try {
                val events = eventRepository.deleteEvent(event, uid)
                if (events.data == false) {
                    getEvents(uid)
                } else {
                    _eventState.value = EventState.Empty
                }
            } catch (e: Exception){
                _eventState.value = EventState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun addEvent(uid: String, event: Event){
        eventJob?.cancel()
        eventJob = viewModelScope.launch {
            _eventState.value = EventState.Loading
            try {
                val events = eventRepository.createEvent(event, uid)
                if (events != null) {
                    getEvents(uid)
                } else {
                    _eventState.value = EventState.Empty
                }
            } catch (e: Exception){
                _eventState.value = EventState.Error(e.message ?: "Unknown error")
            }
        }
    }
}