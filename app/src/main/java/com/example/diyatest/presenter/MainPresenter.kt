package com.example.diyatest.presenter

import com.example.diyatest.BuildConfig
import com.example.diyatest.data.ErrorTypes
import com.example.diyatest.data.WeatherResponseData
import com.example.diyatest.network.OpenWeatherApi
import com.example.diyatest.view.MainView
import retrofit2.Call
import javax.inject.Inject
import retrofit2.Callback
import retrofit2.Response

class MainPresenter(val view: MainView) {

    @Inject
    lateinit var api: OpenWeatherApi

    fun getDailyForecast(cityName: String) {
        view.showSpinner()
        api.dailyForecast(cityName).enqueue(object : Callback<WeatherResponseData> {

            override fun onResponse(
                call: Call<WeatherResponseData>,
                response: Response<WeatherResponseData>
            ) {
                response.body()?.let {
                    createListForView(it)
                    view.hideSpinner()
                } ?: view.showErrorToast(ErrorTypes.NO_RESULT_FOUND)
            }

            override fun onFailure(call: Call<WeatherResponseData>?, t: Throwable) {
                view.showErrorToast(ErrorTypes.CALL_ERROR)
            }
        })
    }

    private fun createListForView(weatherResponse: WeatherResponseData) {
        view.updateForecast(weatherResponse)
    }
}
