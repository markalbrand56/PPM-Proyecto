package com.uvg.todoba.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uvg.todoba.data.remote.dto.CategoryDTO

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
)

fun Category.toDTO() = CategoryDTO(
    id = id,
    name = name,
)
