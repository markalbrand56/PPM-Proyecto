package com.uvg.todoba.data.repository.event

import com.uvg.todoba.data.local.entity.Event

interface EventRepository {
    suspend fun createEvent(event: Event, userID: String)
    suspend fun updateEvent(event: Event, userID: String)
    suspend fun deleteEvent(event: Event, userID: String) //
    suspend fun getEventById(id: Int, userID: String): Event?
    suspend fun getEvents(userID: String): List<Event>?
    suspend fun deleteAllEvents(userID: String) //
}