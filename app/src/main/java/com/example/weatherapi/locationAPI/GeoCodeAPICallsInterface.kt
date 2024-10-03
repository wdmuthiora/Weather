package com.example.weatherapi.locationAPI

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodeAPICallsInterface {
    @GET("geocode/v1/json")
    suspend fun getReverseGeoCode(
        @Query("q") query: String,
        @Query("key") key: String,
        @Query("limit") limit: Int = 1
    ): Response<GeoCodeResponse>
}