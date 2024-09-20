package com.theme.lambda.launcher.utils

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.theme.lambda.launcher.Constants

fun String.withHost(): String {
    var path = this
    if (path.startsWith("/")) {
        path = path.substring(1, path.length)
    }
    return "${Constants.BASE_URL}$path"
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