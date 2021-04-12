package com.example.diyatest.di

import android.app.Application
import com.example.diyatest.BuildConfig
import com.example.diyatest.Utils.ModelPreferencesManager

class MyApplication : Application() {
    private val instance_: MyApplication

    init {
        instance_ = this
    }
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            ModelPreferencesManager.with(this)

        }
    }

}