package com.example.weatherapi.locationAPI

import java.lang.Exception

sealed class NetworkResponseOptions<out T>{
    data class Success<T>(val data: T): NetworkResponseOptions<T>()
    data class Error(val exception: Exception): NetworkResponseOptions<Nothing>()
    data class Loading(val isLoading: Boolean): NetworkResponseOptions<Nothing>()
    data class Failure(val code: Int): NetworkResponseOptions<Nothing>()
}
