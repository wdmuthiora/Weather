package com.example.weatherapi.locationAPI

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstanceForGeocode {
    private const val baseURL = "https://api.opencagedata.com"

    private fun getInstance():Retrofit{
        return Retrofit
            .Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val geoCodeApiCallsInterfaceToReverseGeocode: GeoCodeAPICallsInterface = getInstance().create(GeoCodeAPICallsInterface::class.java)
}