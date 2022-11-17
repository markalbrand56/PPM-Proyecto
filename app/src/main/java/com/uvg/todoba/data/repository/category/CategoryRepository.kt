package com.uvg.todoba.data.repository.category

import com.uvg.todoba.data.Resource
import com.uvg.todoba.data.local.entity.Category

interface CategoryRepository {
    suspend fun createCategory(category: Category, userID: String): Resource<Boolean>
    suspend fun getAllCategories(userID: String): List<Category>?
    suspend fun deleteAllCategories(userID: String): Resource<Boolean>
}