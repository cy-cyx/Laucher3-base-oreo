package com.lambda.common.utils

import android.content.Context.MODE_PRIVATE

object SpUtil {
    private val sp by lazy {
        CommonUtil.appContext!!.getSharedPreferences(
            "launcher",
            MODE_PRIVATE
        )
    }

    @JvmStatic
    fun putBool(key: String, value: Boolean) {
        val editor = sp.edit()
        editor?.putBoolean(key, value)
        editor.apply()
    }

    @JvmStatic
    fun getBool(key: String, default: Boolean = false): Boolean {
        return sp?.getBoolean(key, default) ?: default
    }

    @JvmStatic
    fun putInt(key: String, value: Int) {
        val editor = sp.edit()
        editor?.putInt(key, value)
        editor.apply()
    }

    @JvmStatic
    fun getInt(key: String, default: Int = 0): Int {
        return sp?.getInt(key, default) ?: default
    }

    @JvmStatic
    fun putString(key: String, value: String) {
        val editor = sp.edit()
        editor?.putString(key, value)
        editor.apply()
    }

    @JvmStatic
    fun getString(key: String): String {
        return sp?.getString(key, "") ?: ""
    }

    @JvmStatic
    fun putFloat(key: String, value: Float) {
        val editor = sp.edit()
        editor?.putFloat(key, value)
        editor.apply()
    }

    @JvmStatic
    fun getFloat(key: String, default: Float = 0f): Float {
        return sp?.getFloat(key, default) ?: default
    }

    @JvmStatic
    fun putLong(key: String, value: Long) {
        val editor = sp.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    @JvmStatic
    fun getLong(key: String, default: Long = 0L): Long {
        return sp?.getLong(key, default) ?: default
    }
}

fun String.getSpBool(default: Boolean = false) = SpUtil.getBool(this, default)
fun String.putSpBool(value: Boolean) = SpUtil.putBool(this, value)
fun String.getSpInt(default: Int = 0) = SpUtil.getInt(this, default)
fun String.putSpInt(value: Int) = SpUtil.putInt(this, value)
fun String.putSpFloat(value: Float) = SpUtil.putFloat(this, value)
fun String.getSpFloat(default: Float = 0f) = SpUtil.getFloat(this, default)
fun String.putSpLong(value: Long) = SpUtil.putLong(this, value)
fun String.getSpLong(default: Long = 0L) = SpUtil.getLong(this, default)
fun String.putSpString(value: String) = SpUtil.putString(this, value)
fun String.getSpString() = SpUtil.getString(this)