package com.lambdaweather

import android.app.Application
import android.view.ViewGroup
import com.lambdaweather.factory.RetrofitFactory

object LambdaWeather {
    lateinit var application : Application
    lateinit var baseUrl : String

    var showNativeAdCallback : ShowNativeAdCallback ?= null
    fun init(context: Application,baseUrl : String){
        application = context
        this.baseUrl = baseUrl
        RetrofitFactory.init()
    }

    fun showNativeAd(fl : ViewGroup){
        showNativeAdCallback?.show(fl)
    }

    interface ShowNativeAdCallback{
        fun show(fl : ViewGroup)
    }
}