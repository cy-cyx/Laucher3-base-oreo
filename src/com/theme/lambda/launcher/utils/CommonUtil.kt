package com.theme.lambda.launcher.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowManager

object CommonUtil {

    var appContext: Context? = null

    fun dp2px(dpVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dpVal,
            appContext!!.resources.displayMetrics
        ).toInt()
    }

    fun getStatusBarHeight(): Int {
        val resources = appContext?.resources
        val resourceId = resources?.getIdentifier("status_bar_height", "dimen", "android")
        return resources?.getDimensionPixelSize(resourceId ?: 0) ?: 0
    }

    fun getScreenWidth(): Int {
        return appContext?.resources?.displayMetrics?.widthPixels ?: 0
    }

    fun getScreenHeight(): Int {
        return appContext?.resources?.displayMetrics?.heightPixels ?: 0
    }

    fun getDrawable(res: Int): Drawable? {
        return appContext!!.resources?.getDrawable(res)
    }

    fun getColor(res: Int): Int {
        return appContext!!.getColor(res)
    }

    fun getString(res: Int): String {
        return appContext!!.resources.getString(res)
    }
}