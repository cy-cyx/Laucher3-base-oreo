package com.theme.lambda.launcher.utils

import android.view.View
import android.widget.FrameLayout
import com.theme.lambda.launcher.Constants

fun String.withHost(): String {
    var path = this
    if (path.startsWith("/")) {
        path = path.substring(1, path.length)
    }
    return "${Constants.SRC_URL}$path"
}

fun String.requestTag(): String {
    if (this == "Hot") return ""
    return this
}

fun View.marginStatusBarHeight() {
    val lp = this.layoutParams as FrameLayout.LayoutParams
    lp.topMargin = CommonUtil.getStatusBarHeight()
    requestLayout()
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
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