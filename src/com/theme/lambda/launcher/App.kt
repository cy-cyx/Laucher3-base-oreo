package com.theme.lambda.launcher

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.android.launcher3.BuildConfig
import com.android.launcher3.CycleTimer
import com.lambda.common.Constants
import com.lambda.common.http.Global
import com.lambda.common.ad.AdUtil
import com.theme.lambda.launcher.appinfo.AppInfoCache
import com.theme.lambda.launcher.appwidget.utils.WeatherManager
import com.theme.lambda.launcher.netstate.NetStateChangeReceiver
import com.theme.lambda.launcher.service.FirebaseService
import com.lambda.common.statistics.EventUtil
import com.lambda.common.statistics.FirebaseAnalyticsUtil
import com.theme.lambda.launcher.utils.BluetoothUtil
import com.lambda.common.utils.CommonUtil
import com.lambda.common.utils.LogUtil
import com.lambda.common.utils.FirebaseConfigUtil
import com.lambda.common.utils.utilcode.util.ActivityUtils
import com.theme.lambda.launcher.utils.OsUtil
import com.lambda.common.vip.VipManager
import com.lambda.weather.LambdaWeather
import com.theme.lambda.launcher.recall.RecallManager
import com.theme.lambda.launcher.ui.news.NewDetailsActivity
import com.theme.lambda.launcher.ui.news.NewsFragment
import com.theme.lambda.launcher.utils.WeatherTimerUtils

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
                LogUtil.init(BuildConfig.isDebug)
                initWeather()
                Log.d(TAG, "init 1 : ${System.currentTimeMillis() - start}")
                FirebaseAnalyticsUtil.init(this)
                Log.d(TAG, "init 2 : ${System.currentTimeMillis() - start}")
                EventUtil.init(BuildConfig.isDebug)
                Log.d(TAG, "init 3 : ${System.currentTimeMillis() - start}")
                AdUtil.initAd(this, BuildConfig.isDebug, BuildConfig.SECRET_KEY, BuildConfig.FLAVOR)
                AdUtil.clickAdCallback = {
                    ActivityUtils.getTopActivity()?.let {
                        RecallManager.startTimeoutRecall(it)
                    }
                }
                Log.d(TAG, "init 4 : ${System.currentTimeMillis() - start}")
                VipManager.init(BuildConfig.isDebug, BuildConfig.SECRET_KEY)
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


    private fun initWeather() {
        LambdaWeather.init(this)
        LambdaWeather.lambdaWeatherCallback = object : LambdaWeather.LambdaWeatherCallback {
            override fun callUpdateAboutWeather() {
                WeatherTimerUtils.getIpLocation()
                WeatherManager.init()
            }

            override fun getNewFragment(): Fragment {
                return NewsFragment()
            }

            override fun openNewDetailActivity(context: Context, new: String) {
                NewDetailsActivity.start(context, new)
            }

        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        CommonUtil.appContext = this
    }
}