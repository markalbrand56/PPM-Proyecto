package com.uvg.todoba.data.remote.dto

import com.uvg.todoba.data.local.entity.Category

data class CategoryDTO(
    val id: Int,
    val name: String,
) {
    fun toEntity() = Category(
        id = id,
        name = name,
    )
}
