package com.theme.lambda.launcher

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.webkit.WebView
import com.android.launcher3.BuildConfig
import com.android.launcher3.CycleTimer
import com.lambda.common.http.Global
import com.lambdaweather.LambdaWeather
import com.theme.lambda.launcher.ad.AdUtil
import com.theme.lambda.launcher.appinfo.AppInfoCache
import com.theme.lambda.launcher.appwidget.utils.WeatherManager
import com.theme.lambda.launcher.data.di.allModules
import com.theme.lambda.launcher.netstate.NetStateChangeReceiver
import com.theme.lambda.launcher.service.FirebaseService
import com.theme.lambda.launcher.statistics.EventUtil
import com.theme.lambda.launcher.statistics.FirebaseAnalyticsUtil
import com.theme.lambda.launcher.utils.BluetoothUtil
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.FirebaseConfigUtil
import com.theme.lambda.launcher.utils.OsUtil
import com.theme.lambda.launcher.vip.VipManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    private val TAG = "LauncherApp"

    override fun onCreate() {
        super.onCreate()

        val start = System.currentTimeMillis()
        Log.d(TAG, "start")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val process = getProcessName()
            if (packageName != process) WebView.setDataDirectorySuffix(process)
        }
        val processName: String? = OsUtil.getCurProcessName(this)
        if (!TextUtils.isEmpty(processName)) {
            val defaultProcess = processName == packageName
            if (defaultProcess) {
                Global.packageNamesSuffix = BuildConfig.Suffix
                LambdaWeather.init(this, Constants.BASE_URL)
                Log.d(TAG, "init 1 : ${System.currentTimeMillis() - start}")
                FirebaseAnalyticsUtil.init(this)
                Log.d(TAG, "init 2 : ${System.currentTimeMillis() - start}")
                EventUtil.init()
                Log.d(TAG, "init 3 : ${System.currentTimeMillis() - start}")
                AdUtil.initAd(this)
                Log.d(TAG, "init 4 : ${System.currentTimeMillis() - start}")
                VipManager.init()
                Log.d(TAG, "init 5 : ${System.currentTimeMillis() - start}")
                FirebaseConfigUtil.initRemoteConfig()
                Log.d(TAG, "init 6 : ${System.currentTimeMillis() - start}")

                Looper.myQueue().addIdleHandler {
                    CycleTimer.init()
                    FirebaseService.subscribe()
                    NetStateChangeReceiver.registerReceiver(this)
                    AppInfoCache.init(this)
                    BluetoothUtil.init(this)
                    WeatherManager.init()
                    false
                }
            }
        }

        Log.d(TAG, "init time : ${System.currentTimeMillis() - start}")
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        CommonUtil.appContext = this
    }
}