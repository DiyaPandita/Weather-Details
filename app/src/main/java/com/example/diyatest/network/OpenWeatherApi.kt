package com.example.diyatest.network

import com.example.diyatest.data.WeatherResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {
    @GET("weather/")
    fun dailyForecast(@Query("q") cityName : String) : Call<WeatherResponseData>

    companion object {
        val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    }

}