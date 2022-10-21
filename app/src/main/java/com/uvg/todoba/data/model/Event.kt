package com.uvg.todoba.data.model

// Clase para información de prueba
data class Event(
    val title: String,
    val category: String,
    val date: String,
    val time: String,
)

object TestDatabase {
    private val events = mutableListOf(
        Event(
            title = "Comprar jugo de naranja",
            category = "Hogar",
            date = "22/10/2022",
            time = "10:00",
        ),
        Event(
            title = "Cena en casa de Carlos",
            category = "Actividades",
            date = "23/10/2022",
            time = "19:00",
        ),
        Event(
            title = "Hoja de Trabajo 11",
            category = "Universidad",
            date = "24/10/2022",
            time = "8:00",
        ),
        Event(
            title = "Examen Parcial 2",
            category = "Universidad",
            date = "26/10/2022",
            time = "14:00",
        ),
        Event(
            title = "Cumpleaños Marlon",
            category = "Cumpleaños",
            date = "29/10/2022",
            time = "",
        ),
    )
    fun getEvents(): MutableList<Event> {
        return events
    }
}
