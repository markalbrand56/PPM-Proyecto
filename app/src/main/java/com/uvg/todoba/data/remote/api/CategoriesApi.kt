package com.uvg.todoba.data.remote.api

import com.uvg.todoba.data.Resource
import com.uvg.todoba.data.remote.dto.CategoryDTO

interface CategoriesApi {
    suspend fun insert(category: CategoryDTO, userId: String): Resource<Boolean>
    suspend fun getAll(userId: String): List<CategoryDTO>?
    suspend fun deleteAll(userId: String): Resource<Boolean>
}