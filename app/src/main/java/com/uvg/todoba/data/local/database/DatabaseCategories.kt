package com.uvg.todoba.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.uvg.todoba.data.local.entity.Category

@Database(entities = [Category::class], version=1)
abstract class DatabaseCategories: RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
}