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

fun String.getMMKVBool(default: Boolean = false) = SpUtil.getBool(this, default)
fun String.putMMKVBool(value: Boolean) = SpUtil.putBool(this, value)
fun String.getMMKVInt(default: Int = 0) = SpUtil.getInt(this, default)
fun String.putMMKVInt(value: Int) = SpUtil.putInt(this, value)
fun String.putMMKVFloat(value: Float) = SpUtil.putFloat(this, value)
fun String.getMMKVFloat(default: Float = 0f) = SpUtil.getFloat(this, default)
fun String.putMMKVLong(value: Long) = SpUtil.putLong(this, value)
fun String.getMMKVLong(default: Long = 0L) = SpUtil.getLong(this, default)
fun String.putMMKVString(value: String) = SpUtil.putString(this, value)
fun String.getMMKVString() = SpUtil.getString(this)