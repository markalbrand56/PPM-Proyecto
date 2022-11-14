package com.uvg.todoba.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.uvg.todoba.data.local.entity.Event

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Update
    suspend fun updateEvent(event: Event)

    @Query("DELETE FROM event WHERE id = :id")
    suspend fun deleteEvent(id: Int)

    @Query("SELECT * FROM event WHERE id = :id")
    suspend fun getEventById(id: Int): Event?

    @Query("SELECT * FROM event")
    suspend fun getEvents(): List<Event>

    @Query("DELETE FROM event")
    suspend fun deleteAllEvents()

}
