package com.example.weatherapi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapi.api.NetworkResponse
import com.example.weatherapi.api.RetrofitInstanceForWeatherAPI
import com.example.weatherapi.api.WeatherApiModel
import com.example.weatherapi.locationAPI.GeoCodeResponse
import com.example.weatherapi.locationAPI.NetworkResponseOptions
import com.example.weatherapi.locationAPI.RetrofitInstanceForGeocode
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {

    private val weatherAPI = RetrofitInstanceForWeatherAPI.weatherAPIInterface
    private val reverseGeocodeAPI = RetrofitInstanceForGeocode.geoCodeApiCallsInterfaceToReverseGeocode

    private val _weather = MutableLiveData<NetworkResponse<WeatherApiModel>>()
    val weatherResult: LiveData<NetworkResponse<WeatherApiModel>> = _weather

    private val _geoCodeResponse = MutableLiveData<NetworkResponseOptions<GeoCodeResponse>>()
    //val geoCodeResponse: LiveData<NetworkResponseOptions<GeoCodeResponse>> = _geoCodeResponse

    private var _locationName = MutableLiveData<String>()
    val locationName: LiveData<String> = _locationName

    private var locationUpdated: Boolean = false

    fun updateLocation(latitude: Double, longitude: Double, key: String){
        locationUpdated = true
        getLocationName(key, latitude, longitude)
    }

    fun getWeatherData(key:String, city: String){
        _weather.value = NetworkResponse.Loading(isLoading = true)
        viewModelScope.launch {
            val response = weatherAPI.getWeather(key.substring(1, key.length - 1), city)
            try {
                if (response.isSuccessful){
                    response.body()?.let {
                        _weather.value = NetworkResponse.Success(it)
                    }
                }else {
                    _weather.value = NetworkResponse.Failure(1)
                }
            }catch (exception: Exception){
                _weather.value = NetworkResponse.Error(exception.message.toString())
            }
        }
    }

    private fun getLocationName(key: String, latitude: Double, longitude: Double){

        if (locationUpdated) {
            //_geoCodeResponse.value = NetworkResponseOptions.Loading(isLoading = true)
            viewModelScope.launch {
                val response = reverseGeocodeAPI.getReverseGeoCode(
                    "$latitude,$longitude",
                    key.substring(1, key.length - 1)
                )
                try {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            //_geoCodeResponse.value = NetworkResponseOptions.Success(it)
                            _locationName.value = it.results[0].components.city + ", "+ it.results[0].components.country
                        }
                    } else {
                        _geoCodeResponse.value = NetworkResponseOptions.Failure(1)
                    }
                } catch (exception: Exception) {
                    _geoCodeResponse.value = NetworkResponseOptions.Error(exception)
                }
            }
        }
   }

}

