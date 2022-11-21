package com.uvg.todoba.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.todoba.data.Resource
import com.uvg.todoba.data.local.entity.Category
import com.uvg.todoba.data.remote.dto.CategoryDTO
import com.uvg.todoba.data.repository.category.CategoryRepository
import com.uvg.todoba.ui.viewmodels.states.CategoryState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class CategoryViewModel(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val _categoryState = MutableStateFlow<CategoryState>(CategoryState.Empty)
    val categoryState: StateFlow<CategoryState> = _categoryState
    private var eventJob: Job? = null

    fun getCategories(uid: String) {
        eventJob?.cancel()
        eventJob = viewModelScope.launch {
            _categoryState.value = CategoryState.Loading
            try {
                val categories = categoryRepository.getAllCategories(uid)
                if (categories != null) {
                    _categoryState.value = CategoryState.Updated(categories.toMutableList())
                    _categoryState.value = CategoryState.Empty
                } else {
                    _categoryState.value = CategoryState.Empty
                }
            } catch (e: Exception) {
                _categoryState.value = CategoryState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun addCategory(uid: String, name: String) {
        eventJob?.cancel()
        eventJob = viewModelScope.launch {
            _categoryState.value = CategoryState.Loading
            try {
                val category = Category(
                    firebaseId = UUID.randomUUID().toString(),
                    name = name
                )
                val categories = categoryRepository.createCategory(category, uid)
                if (categories != null) {
                    getCategories(uid)
                } else {
                    _categoryState.value = CategoryState.Empty
                }
            } catch (e: Exception) {
                _categoryState.value = CategoryState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteAllCategories(uid: String) {
        eventJob?.cancel()
        eventJob = viewModelScope.launch {
            _categoryState.value = CategoryState.Loading
            try {
                val categories = categoryRepository.deleteAllCategories(uid)
                if (categories is Resource.Success) {
                    _categoryState.value = CategoryState.Empty
                } else {
                    _categoryState.value = CategoryState.Error(categories.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _categoryState.value = CategoryState.Error(e.message ?: "Unknown error")
            }
        }
    }
}