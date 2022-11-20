package com.uvg.todoba.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uvg.todoba.data.remote.dto.CategoryDTO

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val firebaseId: String,
    val name: String,
)

fun Category.toDTO(paramId: Int) = CategoryDTO(
    id = paramId,
    name = name,
    firebaseId = firebaseId,
)
