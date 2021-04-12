package com.example.diyatest.Utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.diyatest.Utils.Constants.Companion.ArrayListCityData
import com.example.diyatest.data.WeatherResponseData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


object ModelPreferencesManager {

    lateinit var preferences: SharedPreferences

    private const val PREFERENCES_FILE_NAME = "PREFERENCES_FILE_NAME"

    fun with(application: Application) {
        preferences = application.getSharedPreferences(
            PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
    }

    fun <T> put(`object`: T, key: String) {
        val jsonString = GsonBuilder().create().toJson(`object`)
        preferences.edit().putString(key, jsonString).apply()
    }

    inline fun <reified T> get(key: String): T? {
        val value = preferences.getString(key, null)
        return GsonBuilder().create().fromJson(value, T::class.java)
    }

    fun saveCityToFavrouites(
        context: Context,
        cities: List<WeatherResponseData?>?
    ) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(
            PREFERENCES_FILE_NAME,
            Context.MODE_PRIVATE
        )
        editor = settings.edit()
        val gson = Gson()
        val jsonUsers = gson.toJson(cities)
        editor.putString(ArrayListCityData, jsonUsers)
        editor.commit()
    }

    fun getFavCitiesList():ArrayList<WeatherResponseData>? {
        val gson = Gson()
        val cityArrayList: ArrayList<WeatherResponseData>
        val json = preferences.getString(ArrayListCityData, null)

        cityArrayList = when {
            json.isNullOrEmpty() -> ArrayList()
            else -> gson.fromJson(json, object : TypeToken<List<WeatherResponseData>>() {}.type)
        }
        return cityArrayList
    }

}
