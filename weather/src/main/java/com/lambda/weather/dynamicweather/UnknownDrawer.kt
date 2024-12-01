package com.lambdaweather.view.dynamicweather

import android.content.Context
import android.graphics.Canvas

class UnknownDrawer constructor(context: Context, isNight: Boolean) : BaseDrawer(context, isNight) {
    override fun drawWeather(canvas: Canvas, alpha: Float): Boolean {
        return true //这里返回false会出现有时候不刷新的问题
    }
}