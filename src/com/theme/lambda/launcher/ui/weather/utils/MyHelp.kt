package com.lambdaweather.utils

import android.text.TextUtils
import kotlin.math.roundToInt


fun String?.toIsEmpty(): String? {
    return if (TextUtils.isEmpty(this) || "null" == this) {
        null
    } else {
        this
    }
}

fun String?.toCustomInt(): String {
    return if (TextUtils.isEmpty(this) || "null" == this) {
        ""
    } else {
        this?.toDouble()?.roundToInt().toString()
    }
}