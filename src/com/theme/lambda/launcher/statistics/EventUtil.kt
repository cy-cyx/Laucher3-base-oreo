package com.theme.lambda.launcher.statistics

import android.os.Bundle
import com.android.launcher3.BuildConfig
import com.lambda.common.event.Event
import com.lambda.common.event.core.InitParam
import com.lambda.common.event.utils.AdjustHelper
import com.lambda.common.utils.utilcode.util.Utils
import com.theme.lambda.launcher.Constants
import com.theme.lambda.launcher.utils.LogUtil
import com.theme.lambda.launcher.utils.SpKey
import com.theme.lambda.launcher.utils.getSpLong
import com.theme.lambda.launcher.utils.putSpLong


object EventUtil {

    private var TAG = "EventUtil"

    private var init = false

    // 一些防止重复上报的标志位
    var hasLogHomeChangeTag = false
    var hasLogHomeScroll = false

    fun init() {
        if (init) {
            return
        }
        init = true
        Event.init(InitParam.Builder(Constants.BASE_URL, "").apply {
            adOrigin(true)
        }.build())

        AdjustHelper.setDebug(BuildConfig.isDebug)
        AdjustHelper.setAttributionListener {
        }
        AdjustHelper.initSDK(Utils.getApp().applicationContext, Constants.adjustToken)

        logInstallEvent()
    }

    fun logEvent(eventName: String, bundle: Bundle) {
        LogUtil.d(TAG, "${eventName} : ${bundle}")
        Event.logEvent(eventName, bundle)
    }

    private fun logInstallEvent() {
        if (SpKey.install_time.getSpLong() == 0L) {
            SpKey.install_time.putSpLong(System.currentTimeMillis())
            logEvent(EventName.AppInstall, Bundle())
        }
    }
}