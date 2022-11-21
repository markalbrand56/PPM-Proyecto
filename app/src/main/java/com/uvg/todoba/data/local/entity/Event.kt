package com.uvg.todoba.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uvg.todoba.data.remote.dto.EventDTO

@Entity
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val firestoreId: String,
    val title: String,
    val category: String,
    val date: String,
    val time: String,
    val location: String,
    val description: String,
): java.io.Serializable

fun Event.toDTO(roomID: Int) = EventDTO(
    id = roomID,
    firestoreId = firestoreId,
    title = title,
    category = category,
    date = date,
    time = time,
    location = location,
    description = description,
)