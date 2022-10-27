package com.uvg.todoba.data.local.database

import androidx.room.Dao
import androidx.room.Query
import com.uvg.todoba.data.model.Event

@Dao
interface EventDao {
    @Query("SELECT * FROM event")
    suspend fun getEvents(): List<Event>

    @Query("SELECT * FROM event WHERE id = :id")
    suspend fun getEventById(id: Int): Event?

}
