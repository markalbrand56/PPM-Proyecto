package com.uvg.todoba.data.remote.dto

import com.uvg.todoba.data.local.entity.Event

data class EventDTO(
    val id: Int,
    val title: String,
    val category: String,
    val date: String,
    val time: String,
    val location: String,
    val description: String,
)

fun EventDTO.toEntity() = Event(
    id = id,
    title = title,
    category = category,
    date = date,
    time = time,
    location = location,
    description = description,
)
