package com.example.papb5.data.location

data class DeviceLocation(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float? = null,
    val isPrecise: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
