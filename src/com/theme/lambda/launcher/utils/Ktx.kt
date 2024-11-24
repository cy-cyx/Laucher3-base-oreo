package com.theme.lambda.launcher.utils

import android.view.View

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