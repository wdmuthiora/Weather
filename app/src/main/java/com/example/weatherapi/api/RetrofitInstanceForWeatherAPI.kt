package com.example.weatherapi.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object  RetrofitInstanceForWeatherAPI {

    const val baseUrl = "https://api.weatherapi.com"

    private fun createRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val weatherAPIInterface: WeatherAPIInterface = createRetrofitInstance().create(WeatherAPIInterface::class.java)

}