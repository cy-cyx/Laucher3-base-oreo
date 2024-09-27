package com.theme.lambda.launcher.utils

import android.R
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue

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

    fun getActionBarHeight(): Int {
        val tv = TypedValue()
        if (appContext!!.getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(
                tv.data, appContext!!.getResources().getDisplayMetrics()
            )
        }
        return 0
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