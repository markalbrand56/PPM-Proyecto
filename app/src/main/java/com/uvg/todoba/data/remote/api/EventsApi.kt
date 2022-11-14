package com.uvg.todoba.data.remote.api

import com.uvg.todoba.data.remote.dto.EventDTO

interface EventsApi {
    suspend fun insert(event: EventDTO, userId: String)
    suspend fun update(event: EventDTO, userId: String)
    suspend fun getById(id: Int, userId: String): EventDTO?
    suspend fun getAll(userId: String): List<EventDTO>?
    suspend fun deleteById(id: Int, userId: String)
    suspend fun deleteAll(userId: String)
}