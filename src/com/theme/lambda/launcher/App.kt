package com.theme.lambda.launcher

import android.app.Application
import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.webkit.WebView
import com.lambdaweather.LambdaWeather
import com.theme.lambda.launcher.ad.AdUtil
import com.theme.lambda.launcher.data.di.allModules
import com.theme.lambda.launcher.statistics.EventUtil
import com.theme.lambda.launcher.statistics.FirebaseAnalyticsUtil
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.OsUtil
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val start = System.currentTimeMillis()
        Log.d("App", "start")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val process = getProcessName()
            if (packageName != process) WebView.setDataDirectorySuffix(process)
        }
        val processName: String? = OsUtil.getCurProcessName(this)
        if (!TextUtils.isEmpty(processName)) {
            val defaultProcess = processName == packageName
            if (defaultProcess) {
                initKoin()
                LambdaWeather.init(this, Constants.BASE_URL)
                FirebaseAnalyticsUtil.init(this)
                EventUtil.init()
                AdUtil.initAd(this)
            }
        }

        Log.d("App", "init time : ${System.currentTimeMillis() - start}")
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