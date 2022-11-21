package com.uvg.todoba.data.remote.api

import com.uvg.todoba.data.Resource
import com.uvg.todoba.data.remote.dto.EventDTO

interface EventsApi {
    suspend fun insert(event: EventDTO, userId: String): Resource<Boolean>
    suspend fun update(event: EventDTO, userId: String): Resource<Boolean>
    suspend fun getById(id: String, userId: String): Resource<EventDTO?>
    suspend fun getAll(userId: String): Resource<List<EventDTO>?>
    suspend fun deleteById(id: String, userId: String): Resource<Boolean>
    suspend fun deleteAll(userId: String): Resource<Boolean>
}