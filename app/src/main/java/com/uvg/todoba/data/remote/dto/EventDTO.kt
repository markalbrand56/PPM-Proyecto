package com.uvg.todoba.data.remote.dto

import androidx.room.PrimaryKey
import com.uvg.todoba.data.local.entity.Event

data class EventDTO(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String = "",
    val category: String = "",
    val date: String = "",
    val time: String = "",
    val location: String = "",
    val description: String = "",
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
