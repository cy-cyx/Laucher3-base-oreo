package com.lambdaweather.utils

import java.util.*

object LocalUtils {
    fun getCurrentLanguage(): String {
        return Locale.getDefault().language
    }

    fun getCountry(): String {
        return Locale.getDefault().country
    }
}