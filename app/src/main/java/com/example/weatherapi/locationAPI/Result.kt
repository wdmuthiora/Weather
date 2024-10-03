package com.example.weatherapi.locationAPI

data class Result(
    val bounds: Bounds,
    val components: Components,
    val confidence: Int,
    val distance_from_q: DistanceFromQ,
    val formatted: String,
    val geometry: Geometry
)