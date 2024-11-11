package com.theme.lambda.launcher.utils

import android.util.Log
import com.android.launcher3.BuildConfig

object LogUtil {
    private val isDebug = BuildConfig.isDebug

    @JvmStatic
    fun d(tag: String, msg: String) {
        if (!isDebug) return
        Log.d(tag, msg)
    }

    @JvmStatic
    fun i(tag: String, msg: String) {
        if (!isDebug) return
        Log.i(tag, msg)
    }

    @JvmStatic
    fun e(tag: String, msg: String) {
        if (!isDebug) return
        Log.e(tag, msg)
    }
}