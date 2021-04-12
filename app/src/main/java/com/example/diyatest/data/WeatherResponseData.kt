package com.example.diyatest.data

import com.example.weatherapp.data.Main
import com.example.weatherapp.data.Sys
import com.example.weatherapp.data.Weather
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WeatherResponseData(
    @SerializedName("name") var name: String,
    @SerializedName("weather") var forecast: List<Weather>,
    @SerializedName("sys")  var sys: Sys,
    @SerializedName("main") var main: Main? = null,
    @SerializedName("dt") var date: String? = null
):Serializable