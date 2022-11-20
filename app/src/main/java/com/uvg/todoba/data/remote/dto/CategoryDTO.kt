package com.uvg.todoba.data.remote.dto

import androidx.room.PrimaryKey
import com.uvg.todoba.data.local.entity.Category

data class CategoryDTO(
    val name: String = "",
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
) {
    fun toEntity() = Category(
        id = id,
        name = name,
    )
}
