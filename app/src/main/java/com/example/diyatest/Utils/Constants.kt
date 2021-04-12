package com.example.diyatest.Utils

import com.example.diyatest.data.WeatherResponseData

class Constants {
    companion object {
        const val WeatherDataList = "weather_data_arraylist"
        const val SavedCity = "savedCity"
        const val ArrayListCityData = "arraylist_cities"
        const val SELECTED_WEATHER = "selected_weather"
        const val ORIENTATION_SAVE_DATA = "orientation_change_data"
        var saveDestinationCities: MutableList<WeatherResponseData> = ArrayList()
    }
}