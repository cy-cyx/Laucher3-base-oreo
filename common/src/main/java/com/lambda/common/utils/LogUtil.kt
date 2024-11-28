package com.lambda.common.utils

import android.util.Log

object LogUtil {

    fun init(debug: Boolean) {
        isDebug = debug
    }

    private var isDebug = false

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