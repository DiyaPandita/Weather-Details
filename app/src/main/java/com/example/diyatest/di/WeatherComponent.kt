package com.example.diyatest.di

import com.example.diyatest.presenter.MainPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(WeatherModule::class))
interface WeatherComponent {
    fun inject(presenter: MainPresenter);
}
