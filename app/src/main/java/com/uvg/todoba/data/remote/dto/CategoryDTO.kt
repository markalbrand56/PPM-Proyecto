package com.uvg.todoba.data.remote.dto

import com.uvg.todoba.data.local.entity.Category

data class CategoryDTO(
    val name: String = "",
    val id: Int = 0,
    val firebaseId: String = "",
) {
    fun toEntity() = Category(
        name = name,
        firebaseId = firebaseId,
    )
}
