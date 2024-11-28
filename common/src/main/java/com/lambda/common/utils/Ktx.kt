package com.lambda.common.utils

import android.view.View
import android.widget.FrameLayout

private var lastClickTimeStamp = -1L
private var sLimitDoubleInterval = 300

fun View?.noDoubleClick(block: ((View) -> Unit)) {
    this?.setOnClickListener {
        if (System.currentTimeMillis() - lastClickTimeStamp > sLimitDoubleInterval) {
            lastClickTimeStamp = System.currentTimeMillis()
            block.invoke(it)
        }
    }
}

fun View.marginStatusBarHeight() {
    val lp = this.layoutParams as FrameLayout.LayoutParams
    lp.topMargin = com.lambda.common.utils.CommonUtil.getStatusBarHeight()
    requestLayout()
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}