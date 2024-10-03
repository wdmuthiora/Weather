package com.example.weatherapi.locationAPI

data class Components(
    val ISO_3166_1_alph_2: String,
    val ISO_3166_1_alpha_3: String,
    val ISO_3166_2: List<String>,
    val _category: String,
    val _normalized_city: String,
    val _type: String,
    val city: String,
    val city_district: String,
    val continent: String,
    val country: String,
    val country_code: String,
    val county: String,
    val house_number: String,
    val office: String,
    val political_union: String,
    val postcode: String,
    val road: String,
    val state: String,
    val state_code: String,
    val suburb: String
)