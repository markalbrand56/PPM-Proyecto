package com.uvg.todoba.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.todoba.data.Resource
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

    fun addEvent(uid: String, event: Event){
        eventJob?.cancel()
        eventJob = viewModelScope.launch {
            _eventState.value = EventState.Loading
            try {
                val events = eventRepository.createEvent(event, uid)
                if (events is Resource.Success) {
                    getEvents(uid)
                } else {
                    _eventState.value = EventState.Error("Error al crear el evento")
                }
            } catch (e: Exception){
                _eventState.value = EventState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateEvent(uid: String, event: Event){
        eventJob?.cancel()
        eventJob = viewModelScope.launch {
            _eventState.value = EventState.Loading
            try {
                val events = eventRepository.updateEvent(event, uid)
                if (events is Resource.Success) {
                    getEvents(uid)
                } else {
                    _eventState.value = EventState.Error("Error al actualizar el evento")
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
                    _eventState.value = EventState.Error("Error al eliminar el evento")
                }
            } catch (e: Exception){
                _eventState.value = EventState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun getEvents(uid: String){
        eventJob?.cancel()
        eventJob = viewModelScope.launch {
            _eventState.value = EventState.Loading
            try {
                val events = eventRepository.getEvents(uid)
                if (events is Resource.Success) {
                    _eventState.value = EventState.Updated(events.data!!)
                    _eventState.value = EventState.Empty
                } else {
                    _eventState.value = EventState.Error("Error al obtener los eventos")
                }
            } catch (e: Exception){
                _eventState.value = EventState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun getEvent(uid: String, id: String){
        eventJob?.cancel()
        eventJob = viewModelScope.launch {
            _eventState.value = EventState.Loading
            try {
                val events = eventRepository.getEventByFireStoreId(id, uid)
                if (events is Resource.Success) {
                    _eventState.value = EventState.Fetched(events.data!!)
                } else {
                    _eventState.value = EventState.Error("Event not found")
                }
            } catch (e: Exception){
                _eventState.value = EventState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun clearAllEvents(uid: String){
        eventJob?.cancel()
        eventJob = viewModelScope.launch {
            _eventState.value = EventState.Loading
            try {
                val events = eventRepository.clearAllEvents(uid)
                if (events is Resource.Success) {
                    _eventState.value = EventState.Updated(mutableListOf())
                    _eventState.value = EventState.Empty
                } else {
                    _eventState.value = EventState.Error("Error al limpiar los eventos")
                }
            } catch (e: Exception){
                _eventState.value = EventState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteAllEvents(uid: String){
        eventJob?.cancel()
        eventJob = viewModelScope.launch {
            _eventState.value = EventState.Loading
            try {
                val events = eventRepository.deleteAllEvents(uid)
                if (events is Resource.Success) {
                    _eventState.value = EventState.Empty
                } else {
                    _eventState.value = EventState.Error("Error al eliminar los eventos")
                }
            } catch (e: Exception){
                _eventState.value = EventState.Error(e.message ?: "Unknown error")
            }
        }
    }
}