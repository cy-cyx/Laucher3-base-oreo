package com.lambdaweather.view.dynamicweather

import android.content.Context
import android.graphics.Canvas

class DefaultDrawer constructor(context: Context) : BaseDrawer(context, true) {
    override fun drawWeather(canvas: Canvas, alpha: Float): Boolean {
        return false
    }// TODO Auto-generated method stub

    override fun getSkyBackgroundGradient(): IntArray {
        return SkyBackground.BLACK
    }

}