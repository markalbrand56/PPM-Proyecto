package com.uvg.todoba.data.local.database

import androidx.room.*
import com.uvg.todoba.data.local.entity.Category

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Query("SELECT * FROM category")
    suspend fun getCategories(): List<Category>

    @Query("DELETE FROM category")
    suspend fun deleteAllCategories()
}