package com.theme.lambda.launcher.utils

import android.util.Log
import com.android.launcher3.BuildConfig

object LogUtil {
    private val isDebug = BuildConfig.isDebug

    fun d(tag: String, msg: String) {
        if (!isDebug) return
        Log.d(tag, msg)
    }

    fun i(tag: String, msg: String) {
        if (!isDebug) return
        Log.i(tag, msg)
    }

    fun e(tag: String, msg: String) {
        if (!isDebug) return
        Log.e(tag, msg)
    }
}