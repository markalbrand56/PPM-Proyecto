package com.uvg.todoba.data.repository.category

import com.uvg.todoba.data.Resource
import com.uvg.todoba.data.local.database.CategoryDao
import com.uvg.todoba.data.local.entity.Category
import com.uvg.todoba.data.local.entity.toDTO
import com.uvg.todoba.data.remote.api.CategoriesApi

class CategoryRepositoryImpl(
    private val categoriesApi: CategoriesApi,
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override suspend fun createCategory(category: Category, userID: String): Resource<Boolean> {
        /*val result = categoriesApi.insert(category.toDTO(), userID)
        if (result is Resource.Success) {
            categoryDao.insertCategory(category)
        }*/
        val newId = categoryDao.insertCategory(category)
        if (newId >= 0) {
            val result = categoriesApi.insert(category.toDTO(newId.toInt()), userID)
            if (result is Resource.Success) {
                return Resource.Success(true)
            }
        }
        return Resource.Error("Error")
    }

    override suspend fun getAllCategories(userID: String): List<Category>? {
        val categories = categoriesApi.getAll(userID)
        if (categories != null) {  // Siempre se actualiza de internet
            for (category in categories) {
                categoryDao.insertCategory(category.toEntity())
            }
        }
        return categoryDao.getCategories()
    }

    override suspend fun deleteAllCategories(userID: String): Resource<Boolean> {
        return try {
            // Solo se borra de la base de datos local
            // Se usa al cerrar sesión
            categoryDao.deleteAllCategories()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error")
        }
    }
}