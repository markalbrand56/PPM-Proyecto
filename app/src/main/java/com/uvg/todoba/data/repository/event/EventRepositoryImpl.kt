package com.uvg.todoba.data.repository.event

import com.uvg.todoba.data.Resource
import com.uvg.todoba.data.local.database.EventDao
import com.uvg.todoba.data.local.entity.Event
import com.uvg.todoba.data.local.entity.toDTO
import com.uvg.todoba.data.remote.api.EventsApi
import com.uvg.todoba.data.remote.dto.toEntity

class EventRepositoryImpl(
    private val api: EventsApi,
    private val eventDao: EventDao
): EventRepository {
    override suspend fun createEvent(event: Event, userID: String): Resource<Boolean> {
        return try {
            val newId = eventDao.insertEvent(event)
            if (newId >= 0) {
                val result = api.insert(event.toDTO(newId.toInt()), userID)
                if (result is Resource.Success) {
                    return Resource.Success(true)
                }
            }
            Resource.Error("Error")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error")
        }
    }

    override suspend fun updateEvent(event: Event, userID: String): Resource<Boolean> {
        return try {
            val result = api.update(event.toDTO(event.id!!), userID)
            if (result is Resource.Success) {
                eventDao.updateEvent(event)
            } else if (result is Resource.Error) {
                return Resource.Error(result.message ?: "Error")
            }
            result
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error")
        }
    }

    override suspend fun deleteEvent(event: Event, userID: String): Resource<Boolean> {
        return try {
            val result = api.deleteById(event.id!!, userID)
            if (result is Resource.Success) {
                eventDao.deleteEvent(event.id!!)
            } else if (result is Resource.Error) {
                return Resource.Error(result.message ?: "Error")
            }
            result
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error")
        }
    }

    override suspend fun getEventById(id: Int, userID: String): Resource<Event?> {
        // Primero se intenta obtener de la base de datos local, si no se prueba con Firestore
        val event = eventDao.getEventById(id)
        if (event == null) {
            val eventDTO = api.getById(id, userID)
            return if (eventDTO != null) {
                eventDao.insertEvent(eventDTO.toEntity())
                Resource.Success(eventDTO.toEntity())
            } else{
                Resource.Error("Error")
            }
        }
        return Resource.Success(event)
    }

    override suspend fun getEvents(userID: String): Resource<List<Event>?> {
        // Primero se intenta obtener de la base de datos local, si no se prueba con Firestore
        val events = eventDao.getEvents()
        if (events.isEmpty()) {
            val eventsDTO = api.getAll(userID)
            if (eventsDTO != null) {
                for (event in eventsDTO) {
                    eventDao.insertEvent(event.toEntity())
                }
                return Resource.Success(eventsDTO.map { it.toEntity() })
            } else{
                return Resource.Error("Error")
            }
        }
/*        val eventsApi = api.getAll(userID)
        val events = if (eventsApi != null) {
            for (event in eventsApi) {
                db.insertEvent(event.toEntity())
            }
            eventsApi.map { it.toEntity() }
        } else {
            null
        }*/
        return Resource.Success(events)
    }

    override suspend fun clearAllEvents(userID: String): Resource<Boolean> {
        return try {
            // Solo se elimina de la base de datos local
            // Se usa al cerrar sesión, no hay opción de eliminar todos los eventos de Firestore
            eventDao.deleteAllEvents()
            Resource.Success(true)
        }catch (e: Exception) {
            Resource.Error(e.message ?: "Error")
        }
    }

    override suspend fun deleteAllEvents(userID: String): Resource<Boolean> {
        return try {
            val result = api.deleteAll(userID)
            if (result is Resource.Success) {
                eventDao.deleteAllEvents()
            } else if (result is Resource.Error) {
                return Resource.Error(result.message ?: "Error")
            }
            result
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error")
        }
    }


}