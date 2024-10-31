package com.theme.lambda.launcher.utils

import android.content.Context
import android.provider.Settings

object DisplayUtil {

    fun getScreenBrightness(context: Context): Int {
        val contentResolver = context.contentResolver
        val defVal = 125
        return Settings.System.getInt(
            contentResolver,
            Settings.System.SCREEN_BRIGHTNESS, defVal
        )
    }

}