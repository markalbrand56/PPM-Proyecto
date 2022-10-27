package com.uvg.todoba.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uvg.todoba.data.model.Event
import com.uvg.todoba.data.local.converters.Converters

@Database(entities = [Event::class], version=1)
@TypeConverters(Converters::class)
abstract class Database: RoomDatabase(){
    abstract fun eventDato(): EventDao
}