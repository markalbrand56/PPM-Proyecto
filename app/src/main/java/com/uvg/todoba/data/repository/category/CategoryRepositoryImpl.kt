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
        val result = categoriesApi.insert(category.toDTO(), userID)
        if (result is Resource.Success) {
            categoryDao.insertCategory(category)
        }
        return result
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
        val result = categoriesApi.deleteAll(userID)
        if (result is Resource.Success) {
            categoryDao.deleteAllCategories()
        }
        return result
    }
}