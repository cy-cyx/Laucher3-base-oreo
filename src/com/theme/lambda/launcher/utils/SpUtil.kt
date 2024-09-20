package com.theme.lambda.launcher.utils

import android.content.Context.MODE_PRIVATE

object SpUtil {
    private val sp by lazy {
        CommonUtil.appContext!!.getSharedPreferences(
            "launcher",
            MODE_PRIVATE
        )
    }

    fun putBool(key: String, value: Boolean) {
        val editor = sp.edit()
        editor?.putBoolean(key, value)
        editor.apply()
    }

    fun getBool(key: String, default: Boolean = false): Boolean {
        return sp?.getBoolean(key, default) ?: default
    }

    fun putInt(key: String, value: Int) {
        val editor = sp.edit()
        editor?.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String, default: Int = 0): Int {
        return sp?.getInt(key, default) ?: default
    }

    fun putString(key: String, value: String) {
        val editor = sp.edit()
        editor?.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String {
        return sp?.getString(key, "") ?: ""
    }

    fun putFloat(key: String, value: Float) {
        val editor = sp.edit()
        editor?.putFloat(key, value)
        editor.apply()
    }

    fun getFloat(key: String, default: Float = 0f): Float {
        return sp?.getFloat(key, default) ?: default
    }

    fun putLong(key: String, value: Long) {
        val editor = sp.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun getLong(key: String, default: Long = 0L): Long {
        return sp?.getLong(key, default) ?: default
    }
}