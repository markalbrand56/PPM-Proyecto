package com.uvg.todoba.data.repository.event

import com.uvg.todoba.data.local.database.EventDao
import com.uvg.todoba.data.local.entity.Event
import com.uvg.todoba.data.local.entity.toDTO
import com.uvg.todoba.data.remote.api.EventsApi
import com.uvg.todoba.data.remote.dto.toEntity

class EventRepositoryImpl(
    private val api: EventsApi,
    private val db: EventDao
): EventRepository {
    override suspend fun createEvent(event: Event, userID: String) {
        api.insert(event.toDTO(), userID)
        db.insertEvent(event)
    }

    override suspend fun updateEvent(event: Event, userID: String) {
        api.update(event.toDTO(), userID)
        db.updateEvent(event)
    }

    override suspend fun deleteEvent(event: Event, userID: String) {
        api.deleteById(event.id, userID)
        db.deleteEvent(event.id)
    }

    override suspend fun getEventById(id: Int, userID: String): Event? {
        // Primero se intenta obtener de la base de datos local, si no se prueba con Firestore
        val event = db.getEventById(id)
        if (event == null) {
            val eventDTO = api.getById(id, userID)
            if (eventDTO != null) {
                db.insertEvent(eventDTO.toEntity())
                return eventDTO.toEntity()
            } else{
                return null
            }
        }
        return event!!
    }

    override suspend fun getEvents(userID: String): List<Event>? {
        // Primero se intenta obtener de la base de datos local, si no se prueba con Firestore
        val events = db.getEvents()
        if (events.isEmpty()) {
            val eventsDTO = api.getAll(userID)
            if (eventsDTO != null) {
                for (event in eventsDTO) {
                    db.insertEvent(event.toEntity())
                }
                return eventsDTO.map { it.toEntity() }
            } else{
                return null
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
        return events
    }

    override suspend fun deleteAllEvents(userID: String) {
        api.deleteAll(userID)
        db.deleteAllEvents()
    }


}