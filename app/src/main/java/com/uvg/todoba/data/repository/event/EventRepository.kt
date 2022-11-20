package com.uvg.todoba.data.repository.event

import com.uvg.todoba.data.Resource
import com.uvg.todoba.data.local.entity.Event

interface EventRepository {
    suspend fun createEvent(event: Event, userID: String): Resource<Boolean>
    suspend fun updateEvent(event: Event, userID: String): Resource<Boolean>
    suspend fun deleteEvent(event: Event, userID: String): Resource<Boolean>
    suspend fun getEventById(id: Int, userID: String): Resource<Event?>
    suspend fun getEvents(userID: String): Resource<List<Event>?>
    suspend fun clearAllEvents(userID: String): Resource<Boolean>
    suspend fun deleteAllEvents(userID: String): Resource<Boolean>
}