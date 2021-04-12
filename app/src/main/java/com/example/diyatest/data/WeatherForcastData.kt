package com.example.diyatest.data

import com.google.gson.annotations.SerializedName

data class ForecastDetail(@SerializedName("main") var  main: String,
                          @SerializedName("timezone") var temperature : Temperature,
                          @SerializedName("weather") var description : List<WeatherDescription>)