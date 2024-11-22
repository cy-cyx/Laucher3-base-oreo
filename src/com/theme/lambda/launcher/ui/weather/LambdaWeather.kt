package com.lambdaweather

import android.app.Application
import com.lambdaweather.factory.RetrofitFactory
import com.theme.lambda.launcher.data.di.allModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

object LambdaWeather {
    lateinit var application: Application

    fun init(context: Application, baseUrl: String) {
        application = context
        RetrofitFactory.init()
        startKoin {
            androidLogger()
            androidContext(context)
            modules(allModules)
        }
    }
}