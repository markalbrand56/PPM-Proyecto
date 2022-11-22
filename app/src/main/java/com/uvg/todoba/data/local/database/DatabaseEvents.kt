package com.uvg.todoba.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.uvg.todoba.data.local.entity.Event

@Database(entities = [Event::class], version=1)
abstract class DatabaseEvents: RoomDatabase(){
    abstract fun eventDao(): EventDao
}