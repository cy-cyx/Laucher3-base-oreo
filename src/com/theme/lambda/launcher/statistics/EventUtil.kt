package com.theme.lambda.launcher.statistics

import android.os.Bundle
import com.lambda.common.event.Event
import com.lambda.common.event.core.InitParam
import com.theme.lambda.launcher.Constants
import com.theme.lambda.launcher.utils.LogUtil
import com.theme.lambda.launcher.utils.SpKey
import com.theme.lambda.launcher.utils.getMMKVLong
import com.theme.lambda.launcher.utils.putMMKVLong


object EventUtil {

    private var TAG = "EventUtil"

    private var init = false
    fun init() {
        if (init) {
            return
        }
        init = true
        Event.init(InitParam.Builder(Constants.BASE_URL, "").apply {
            adOrigin(true)
        }.build())

        logInstallEvent()
    }

    fun logEvent(eventName: String, bundle: Bundle) {
        LogUtil.d(TAG, "${eventName} : ${bundle}")
        Event.logEvent(eventName, bundle)
    }

    private fun logInstallEvent() {
        if (SpKey.install_time.getMMKVLong() == 0L) {
            SpKey.install_time.putMMKVLong(System.currentTimeMillis())
            logEvent(EventName.AppInstall, Bundle())
        }
    }
}