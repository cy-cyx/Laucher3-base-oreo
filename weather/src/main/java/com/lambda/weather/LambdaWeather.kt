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

    fun getNewFragment(): Fragment? {
        return lambdaWeatherCallback?.getNewFragment()
    }

    fun openNewDetailActivity(context: Context, new: String) {
        lambdaWeatherCallback?.openNewDetailActivity(context, new)
    }


    interface LambdaWeatherCallback {
        fun callUpdateAboutWeather()
        fun getNewFragment(): Fragment
        fun openNewDetailActivity(context: Context, new: String)
    }
}