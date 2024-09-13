package com.theme.lambda.launcher

import android.app.Application
import com.lambdaweather.LambdaWeather
import com.theme.lambda.launcher.di.allModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        LambdaWeather.init(this, Constants.BASE_URL)
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(allModules)
        }
    }
}