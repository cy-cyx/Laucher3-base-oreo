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

    /**
     * 已知系统类型时，设置状态栏黑色字体图标。
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     */
    fun setStatusBarLightMode(window: Window) {
        val type = getStatusBarLightMode(window)
        if (type == 1) {
            MIUISetStatusBarLightMode(window, true)
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(window, true)
        } else if (type == 3) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        } else { //5.0
        }
    }

    /**
     * 清除MIUI或flyme或6.0以上版本状态栏黑色字体
     */
    fun setStatusBarDarkMode(window: Window) {
        val type = getStatusBarLightMode(window)
        if (type == 1) {
            MIUISetStatusBarLightMode(window, false)
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(window, false)
        } else if (type == 3) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    /**
     * 设置状态栏黑色字体图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    private fun getStatusBarLightMode(window: Window): Int {
        var result = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MIUISetStatusBarLightMode(window, true)) {
                result = 1
            } else if (FlymeSetStatusBarLightMode(window, true)) {
                result = 2
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                result = 3
            } else { //5.0
            }
        }
        return result
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private fun FlymeSetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (window != null) {
            try {
                val lp = window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                    .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                value = if (dark) {
                    value or bit
                } else {
                    value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                window.attributes = lp
                result = true
            } catch (e: Exception) {
            }
        }
        return result
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private fun MIUISetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (window != null) {
            val clazz: Class<*> = window.javaClass
            try {
                var darkModeFlag = 0
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod(
                    "setExtraFlags",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType
                )
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag) //状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag) //清除黑色字体
                }
                result = true
            } catch (e: Exception) {
            }
        }
        return result
    }
}