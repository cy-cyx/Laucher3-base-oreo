package com.lambda.common.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.TypedValue
import com.lambda.common.utils.utilcode.util.Utils
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.Locale


object CommonUtil {

    @JvmStatic
    var appContext: Application? = null

    @JvmStatic
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

    fun getActionBarHeight(context: Context): Int {
        val tv = TypedValue()
        if (context.getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(
                tv.data, appContext!!.getResources().getDisplayMetrics()
            )
        }
        return 0
    }

    @JvmStatic
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

    fun getRegion(): String {
        val locale = Locale.getDefault()
        return locale.country // This returns the country code (e.g., "US")
    }

    fun isTranslucentOrFloating(activity: Activity): Boolean {
        var isTranslucentOrFloating = false
        try {
            val styleableRes = Class.forName("com.android.internal.R\$styleable")
                .getField("Window")[null] as IntArray
            val ta = activity.obtainStyledAttributes(styleableRes)
            val m: Method = ActivityInfo::class.java.getMethod(
                "isTranslucentOrFloating",
                TypedArray::class.java
            )
            m.setAccessible(true)
            isTranslucentOrFloating = m.invoke(null, ta) as Boolean
            m.setAccessible(false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isTranslucentOrFloating
    }

    fun fixOrientation(activity: Activity?): Boolean {
        try {
            val field: Field = Activity::class.java.getDeclaredField("mActivityInfo")
            field.isAccessible = true
            val o = field.get(activity) as ActivityInfo
            o.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            field.isAccessible = false
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun openWebView(context: Context, url: String) {
        try {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getDrawableIdByName(name: String): Int {
        return appContext!!.getResources()
            .getIdentifier(name, "drawable", Utils.getApp().getPackageName());
    }
}