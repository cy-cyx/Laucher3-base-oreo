package com.lambdaweather.view.dynamicweather

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable

/**
 * 下雪
 */
class SnowDrawer constructor(context: Context, isNight: Boolean) : BaseDrawer(context, isNight) {
    private val drawable: GradientDrawable
    private val holders: ArrayList<SnowHolder> = ArrayList()

    init {
        drawable = GradientDrawable(
            GradientDrawable.Orientation.BL_TR,
            intArrayOf(-0x66000001, 0x00ffffff)
        )
        drawable.shape = GradientDrawable.OVAL
        drawable.gradientType = GradientDrawable.RADIAL_GRADIENT
    }

    override fun drawWeather(canvas: Canvas, alpha: Float): Boolean {
        for (holder: SnowHolder in holders) {
            holder.updateRandom(drawable, alpha)
            drawable.draw(canvas)
        }
        return true
    }

    override fun setSize(width: Int, height: Int) {
        super.setSize(width, height)
        if (holders.size == 0) {
            val minSize: Float = dp2px(MIN_SIZE)
            val maxSize: Float = dp2px(MAX_SIZE)
            val speed: Float = dp2px(80f) // 40当作中雪80
            for (i in 0 until COUNT) {
                val size: Float = BaseDrawer.Companion.getRandom(minSize, maxSize)
                val holder: SnowHolder = SnowHolder(
                    BaseDrawer.Companion.getRandom(0f, width.toFloat()),
                    size,
                    height.toFloat(),
                    speed
                )
                holders.add(holder)
            }
        }
    }

    override fun getSkyBackgroundGradient(): IntArray {
        return if (isNight) SkyBackground.SNOW_N else SkyBackground.SNOW_D
    }

    class SnowHolder constructor(
        var x: Float, // public float y;//y 表示雨滴底部的y坐标,由curTime求得
        val snowSize: Float, // [0,1]
        val maxY: Float, averageSpeed: Float
    ) {
        var curTime // [0,1]
                : Float
        val v // 速度
                : Float

        /**
         * @param x
         * @param snowSize
         * @param maxY
         * @param averageSpeed
         */
        init {
            v = averageSpeed * BaseDrawer.Companion.getRandom(0.85f, 1.15f)
            val maxTime: Float = maxY / v
            curTime = BaseDrawer.Companion.getRandom(0f, maxTime)
        }

        fun updateRandom(drawable: GradientDrawable, alpha: Float) {
            curTime += 0.025f
            val curY: Float = curTime * v
            if ((curY - snowSize) > maxY) {
                curTime = 0f
            }
            val left: Int = Math.round(x - snowSize / 2f)
            val right: Int = Math.round(x + snowSize / 2f)
            val top: Int = Math.round(curY - snowSize)
            val bottom: Int = Math.round(curY)
            drawable.setBounds(left, top, right, bottom)
            drawable.gradientRadius = snowSize / 2.2f
            drawable.alpha = (255 * alpha).toInt()
        }
    }

    companion object {
        val TAG: String = SnowDrawer::class.java.simpleName
        private val COUNT: Int = 30
        private val MIN_SIZE: Float = 12f // dp
        private val MAX_SIZE: Float = 30f // dp
    }
}