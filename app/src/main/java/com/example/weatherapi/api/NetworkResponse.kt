package com.example.weatherapi.api

sealed class NetworkResponse<out T> {
    data class Success<T>(val data: T) : NetworkResponse<T>()
    data class Error(val message: String) : NetworkResponse<Nothing>()
    data class Failure(val code: Int) : NetworkResponse<Nothing>()
    data class Loading(val isLoading: Boolean) : NetworkResponse<Nothing>()
}