package com.uvg.todoba.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uvg.todoba.data.remote.dto.EventDTO

@Entity
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val category: String,
    val date: String,
    val time: String,
    val location: String,
    val description: String,
)

fun Event.toDTO() = EventDTO(
    id = id,
    title = title,
    category = category,
    date = date,
    time = time,
    location = location,
    description = description,
)

object TestDatabase {
    private val events = mutableListOf(
        Event(
            id = 1,
            title = "Comprar jugo de naranja",
            category = "Hogar",
            date = "22/10/2022",
            time = "10:00",
            location = "Supermercado",
            description = "Comprar jugo de naranja para el desayuno",
        ),
        Event(
            id = 2,
            title = "Cena en casa de Carlos",
            category = "Actividades",
            date = "23/10/2022",
            time = "19:00",
            location = "Casa de Carlos",
            description = "Llevar gaseosa y pan",
        ),
        Event(
            id = 3,
            title = "Hoja de Trabajo 11",
            category = "Universidad",
            date = "24/10/2022",
            time = "8:00",
            location = "Aula 1",
            description = "Entregar hoja de trabajo 11",
        ),
        Event(
            id = 4,
            title = "Examen Parcial 2",
            category = "Universidad",
            date = "26/10/2022",
            time = "14:00",
            location = "Aula 12",
            description = "Examen parcial 2 de Programación 2",
        ),
        Event(
            id = 5,
            title = "Cumpleaños Marlon",
            category = "Cumpleaños",
            date = "29/10/2022",
            time = "",
            location = "Casa de Marlon",
            description = "Llevar regalo",
        ),
    )
    fun getEvents(): MutableList<Event> {
        return events
    }
}