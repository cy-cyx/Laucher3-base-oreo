package com.lambdaweather.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager (private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("weather", Context.MODE_PRIVATE)

    fun putString(key: String, item: String) {
        sharedPreferences.edit().putString(key, item).apply()
    }

    fun getString(key: String, default: String): String? {
        return sharedPreferences.getString(key, default)
    }

    companion object {
        // 单例实例
        @Volatile
        private var instance: SharedPreferencesManager? = null

        // 获取单例实例
        fun getInstance(context: Context): SharedPreferencesManager {
            return instance ?: synchronized(this) {
                instance ?: SharedPreferencesManager(context).also { instance = it }
            }
        }
    }

}