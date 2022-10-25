package com.uvg.todoba.data.remote.model.weather

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)