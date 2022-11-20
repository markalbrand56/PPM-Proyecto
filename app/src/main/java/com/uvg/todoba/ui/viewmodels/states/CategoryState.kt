package com.uvg.todoba.ui.viewmodels.states

import com.uvg.todoba.data.local.entity.Category

sealed interface CategoryState{
    data class Updated(val categories: List<Category>): CategoryState
    data class Error(val message: String): CategoryState
    object Empty: CategoryState
    object Loading: CategoryState
}