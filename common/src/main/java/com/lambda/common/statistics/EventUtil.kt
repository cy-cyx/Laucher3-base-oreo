package com.lambda.common.statistics

import android.os.Bundle
import com.lambda.common.event.Event
import com.lambda.common.event.core.InitParam
import com.lambda.common.event.utils.AdjustHelper
import com.lambda.common.utils.getSpLong
import com.lambda.common.utils.putSpLong
import com.lambda.common.utils.utilcode.util.Utils
import com.lambda.common.Constants
import com.lambda.common.utils.LogUtil
import com.lambda.common.utils.SpKey


object EventUtil {

    private var TAG = "EventUtil"

    private var init = false

    // 一些防止重复上报的标志位
    var hasLogHomeChangeTag = false
    var hasLogHomeScroll = false

    var hasLogNewsChangeTag = false
    var hasLogNewsScroll = false

    fun init(debug:Boolean) {
        if (init) {
            return
        }
        init = true
        Event.init(InitParam.Builder(Constants.BASE_URL, "").apply {
            adOrigin(true)
        }.build())
        Event.isDebug = debug

        AdjustHelper.setDebug(debug)
        AdjustHelper.setAttributionListener {
        }
        AdjustHelper.initSDK(Utils.getApp().applicationContext, Constants.adjustToken)

        logInstallEvent()
    }

    fun logEvent(eventName: String, bundle: Bundle, immediately: Boolean = false) {
        LogUtil.d(TAG, "${eventName} : ${bundle}")
        Event.logEvent(eventName, bundle, immediately)
    }

    private fun logInstallEvent() {
        if (SpKey.install_time.getSpLong() == 0L) {
            SpKey.install_time.putSpLong(System.currentTimeMillis())
            logEvent(EventName.AppInstall, Bundle())
        }
    }
}