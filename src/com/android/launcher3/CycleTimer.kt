package com.android.launcher3

import com.theme.lambda.launcher.ad.AdUtil
import com.theme.lambda.launcher.appwidget.WidgetManager
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.concurrent.timer


// 启动一个循环定时器，一小时一次
object CycleTimer {

    private val TAG = "CycleTimer"

    private var callbacks = CopyOnWriteArrayList<CycleTimerCallback>()

    private val onHour = 24 * 60 * 60 * 1000L

    fun addCallback(callback: CycleTimerCallback) {
        callbacks.add(callback)
    }

    fun removeCallback(callback: CycleTimerCallback) {
        callbacks.remove(callback)
    }

    private var init = false
    fun init() {
        if (init) return
        init = true
        timer("cycleTime", false, onHour, onHour) {
            onTime()
            val temp = callbacks
            for (c in temp) {
                c.onOnTime()
            }
        }
    }

    private fun onTime() {
        AdUtil.reloadOpenAdIfNeed()
        WidgetManager.upData()
    }

    interface CycleTimerCallback {
        fun onOnTime()
    }
}