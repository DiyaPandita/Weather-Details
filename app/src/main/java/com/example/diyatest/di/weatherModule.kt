package com.example.diyatest.di

import com.example.diyatest.network.OpenWeatherApi
import com.example.diyatest.network.OpenWeatherInterceptor
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = arrayOf(GSONParsingModule::class))
class WeatherModule {

    @Provides
    @Singleton
    fun provideApi(gson: Gson): OpenWeatherApi {

        val apiClient = OkHttpClient.Builder().addInterceptor(OpenWeatherInterceptor()).build()

        return Retrofit.Builder().apply {
            baseUrl(OpenWeatherApi.BASE_URL)
            addConverterFactory(GsonConverterFactory.create(gson))
            client(apiClient)
        }.build().create(OpenWeatherApi::class.java)
    }
}
