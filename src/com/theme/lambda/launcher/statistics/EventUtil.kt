package com.theme.lambda.launcher.statistics

import android.os.Bundle
import com.lambda.common.event.Event
import com.lambda.common.event.core.InitParam
import com.theme.lambda.launcher.Constants
import com.theme.lambda.launcher.utils.LogUtil


object EventUtil {

    private var TAG = "ClickHouse"

    private var init = false
    fun init() {
        if (init) {
            return
        }
        init = true
        Event.init(InitParam.Builder(Constants.BASE_URL, "").apply {
            adOrigin(true)
            adjustAppToken(Constants.adjustToken)
        }.build())
    }

    fun logEvent(eventName: String, bundle: Bundle) {
        LogUtil.d(TAG, "${eventName} : ${bundle}")
        Event.logEvent(eventName, bundle)
    }
}