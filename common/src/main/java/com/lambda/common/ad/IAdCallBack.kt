package com.lambda.common.ad

interface IAdCallBack {
    fun onNoReady()

    fun onAdClose(status: Int)
}