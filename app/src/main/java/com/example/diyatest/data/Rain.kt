package com.example.weatherapp.data

import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("rain")
    val all: Int = 0
)