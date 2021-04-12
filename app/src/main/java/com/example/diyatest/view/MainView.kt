package com.example.diyatest.view

import com.example.diyatest.data.ErrorTypes
import com.example.diyatest.data.WeatherResponseData

interface MainView {
        fun showSpinner()
        fun hideSpinner()
        fun showError()
        fun showContainer()
        fun updateForecast(forecasts: WeatherResponseData)
        fun showErrorToast(errorType: ErrorTypes)
}