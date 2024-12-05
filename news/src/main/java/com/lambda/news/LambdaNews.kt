package com.lambda.news

import com.lambda.news.data.CategoriesManager

object LambdaNews {

    var lambdaNewsCallback: LambdaNewsCallback? = null

    fun init() {
        CategoriesManager.init()
    }

    fun isInPreviewMode(): Boolean {
        return lambdaNewsCallback?.isInPreviewMode() ?: false
    }

    interface LambdaNewsCallback {
        fun isInPreviewMode(): Boolean
    }
}