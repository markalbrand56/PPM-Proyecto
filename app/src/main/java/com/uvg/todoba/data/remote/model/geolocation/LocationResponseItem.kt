package com.uvg.todoba.data.remote.model.geolocation

data class LocationResponseItem(
    val country: String,
    val lat: Double,
    val local_names: LocalNames,
    val lon: Double,
    val name: String,
    val state: String
)