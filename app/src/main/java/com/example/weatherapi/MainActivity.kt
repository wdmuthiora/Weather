package com.example.weatherapi

import android.Manifest
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapi.ui.theme.WeatherAPITheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var key :String = ""
    private var key2: String = ""

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val ai: ApplicationInfo = applicationContext.packageManager
            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)

        key = ai.metaData["keyValue"].toString()

        key2 = ai.metaData["keyValue2"].toString()

        setContent {
            WeatherAPITheme {
                getLastKnownLocation(viewModel)
                val currentLocation by viewModel.locationName.observeAsState()
                Log.d("Weather-test- setContent", "setContent on Main Activity called "+currentLocation.toString())
                viewModel.getWeatherData(key, currentLocation.toString())
                MainScreen(viewModel, key)
            }
        }
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){ permissions ->
        when{
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION,false) ->{
                Log.d("Permission", "Fine location granted")

            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION,false) ->{
                Log.d("Permission", "Coarse location granted")
            }
            else ->{
                println("No location access granted.")
            }
        }
    }

    private fun getLastKnownLocation(viewModel: WeatherViewModel){

        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    viewModel.updateLocation(location.latitude, location.longitude, key2)
                }?: run {
                    Log.d("Location: ", "Location is null")
                }
            }
            .addOnFailureListener{ exception ->
                Log.d("Location: ", "Error: $exception")
            }

    }

}

