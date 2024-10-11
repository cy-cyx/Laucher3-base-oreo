package com.theme.lambda.launcher.ad

interface IAdCallBack {
    fun onNoReady()

    fun onAdClose(status: Int)
}