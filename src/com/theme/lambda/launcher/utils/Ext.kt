package com.theme.lambda.launcher.utils

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