package com.lambdaweather

import android.app.Application
import android.view.ViewGroup
import com.lambdaweather.factory.RetrofitFactory

object LambdaWeather {
    lateinit var application: Application
    lateinit var baseUrl: String

    fun init(context: Application, baseUrl: String) {
        application = context
        this.baseUrl = baseUrl
        RetrofitFactory.init()
    }
}