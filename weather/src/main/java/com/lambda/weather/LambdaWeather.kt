package com.lambda.weather

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import com.lambda.weather.data.di.allModules
import com.lambdaweather.factory.RetrofitFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

object LambdaWeather {
    lateinit var application: Application
    var lambdaWeatherCallback: LambdaWeatherCallback? = null

    fun init(context: Application) {
        application = context
        RetrofitFactory.init()
        startKoin {
            androidLogger()
            androidContext(context)
            modules(allModules)
        }
    }


    fun callUpdateAboutWeather() {
        lambdaWeatherCallback?.callUpdateAboutWeather()
    }

    fun openNewDetailActivity(context: Context, new: String) {
        lambdaWeatherCallback?.openNewDetailActivity(context, new)
    }

    fun openNewHomeActivity(context: Context){
        lambdaWeatherCallback?.openNewHomeActivity(context)
    }


    interface LambdaWeatherCallback {
        fun callUpdateAboutWeather()
        fun openNewDetailActivity(context: Context, new: String)
        fun openNewHomeActivity(context: Context)
    }
}