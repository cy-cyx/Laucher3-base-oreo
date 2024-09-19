package com.theme.lambda.launcher

import android.app.Application
import android.content.Context
import com.lambda.common.http.HttpProvider
import com.lambdaweather.LambdaWeather
import com.theme.lambda.launcher.data.di.allModules
import com.theme.lambda.launcher.utils.CommonUtil
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        LambdaWeather.init(this, Constants.BASE_URL)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        CommonUtil.appContext = this
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(allModules)
        }
    }
}