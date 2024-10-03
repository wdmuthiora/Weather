package com.example.weatherapi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapi.api.NetworkResponse
import com.example.weatherapi.api.WeatherApiModel

@Composable
fun MainScreen(viewModel: WeatherViewModel, key: String) {
    var city by rememberSaveable {
        mutableStateOf("")
    }
    val weatherResult = viewModel.weatherResult.observeAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 32.dp),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = city,
                onValueChange = {
                    city = it
                },
                label = {
                    Text(text = "Enter city name")
                },

                )
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))

            IconButton(
                onClick = {
                    viewModel.getWeatherData(key, city)
                    keyboardController?.hide()
                }
            ) {
                Icon( modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.Search,
                    contentDescription = "Enter city to search"
                )
            }
        }

        when(val result  = weatherResult.value){
            is NetworkResponse.Loading -> {
                LinearProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is NetworkResponse.Error -> {
                Text(text = "Error")
            }
            is NetworkResponse.Success -> {
                WeatherDetails(data = result.data)
            }
            is NetworkResponse.Failure -> {
                Text(text = "Failure")
            }
            null -> {}
        }

    }
}

@Composable
fun WeatherDetails(data: WeatherApiModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location icon",
                modifier = Modifier.size(40.dp)
            )

            Text(text = data.location.name, fontSize = 20.sp)

            Spacer(modifier = Modifier.padding(horizontal = 4.dp))

            Text(text = data.location.country, fontSize = 20.sp, color = Color. Gray)

        }

        Text(text = data.current.condition.text, fontSize = 20.sp, color = Color.Gray)

        AsyncImage(
            modifier = Modifier.size(100.dp),
            model = "https:${data.current.condition.icon.replace("64x64", "128x128")}",
            contentDescription = "Condition icon"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()){
            Card(modifier = Modifier
                .weight(1f)
                .height(100.dp)) {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Spacer(modifier = Modifier.height(25.dp))
                    WeatherConditionPairs(key = "Local Time", value = data.location.localtime.split(" ")[1])
                }

            }
            Spacer(modifier = Modifier.width(8.dp))
            Card(modifier = Modifier
                .weight(1f)
                .height(100.dp)){
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Spacer(modifier = Modifier.height(25.dp))
                    WeatherConditionPairs(key = "Local Date", value = data.location.localtime.split(" ")[0])
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()){
            Card(modifier = Modifier
                .weight(1f)
                .height(100.dp)) {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Spacer(modifier = Modifier.height(25.dp))
                    WeatherConditionPairs(key = "temp", value = data.current.temp_c + "°C")
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Card(modifier = Modifier
                .weight(1f)
                .height(100.dp)) {
                Column(modifier = Modifier.fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Spacer(modifier = Modifier.height(25.dp))
                    WeatherConditionPairs(key = "wind", value = data.current.wind_kph + " kph")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly){
            Card(modifier = Modifier
                .weight(1f)
                .height(100.dp)){
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Spacer(modifier = Modifier.height(25.dp))
                    WeatherConditionPairs(key = "humidity", value = data.current.humidity + "%")
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Card(modifier = Modifier
                .weight(1f)
                .height(100.dp)) {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Spacer(modifier = Modifier.height(25.dp))
                    WeatherConditionPairs(key = "uv", value = data.current.uv)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly){
            Card(modifier = Modifier
                .weight(1f)
                .height(100.dp)) {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Spacer(modifier = Modifier.height(25.dp))
                    WeatherConditionPairs(key = "precipitation", value = data.current.precip_mm + " mm")
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Card(modifier = Modifier
                .weight(1f)
                .height(100.dp)){
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    WeatherConditionPairs(key = "pressure", value = data.current.pressure_mb + "mb")
                }

            }
        }

    }
}

@Composable
fun WeatherConditionPairs(key: String, value: String){

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 20.sp)
        Text(text = key, color = Color.Gray)
    }
}