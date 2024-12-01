package com.lambdaweather.view.dynamicweather

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable

/**
 * 雨夹雪
 */
class RainAndSnowDrawer constructor(context: Context, isNight: Boolean) :
    BaseDrawer(context, isNight) {
    private val snowDrawable: GradientDrawable
    private val rainDrawable: RainDrawer.RainDrawable
    private val snowHolders: ArrayList<SnowDrawer.SnowHolder> = ArrayList<SnowDrawer.SnowHolder>()
    private val rainHolders: ArrayList<RainDrawer.RainHolder> = ArrayList<RainDrawer.RainHolder>()

    init {
        snowDrawable = GradientDrawable(
            GradientDrawable.Orientation.BL_TR,
            intArrayOf(-0x66000001, 0x00ffffff)
        )
        snowDrawable.shape = GradientDrawable.OVAL
        snowDrawable.gradientType = GradientDrawable.RADIAL_GRADIENT
        rainDrawable = RainDrawer.RainDrawable()
    }

    override fun drawWeather(canvas: Canvas, alpha: Float): Boolean {
        for (holder: SnowDrawer.SnowHolder in snowHolders) {
            holder.updateRandom(snowDrawable, alpha)
            snowDrawable.draw(canvas)
        }
        for (holder: RainDrawer.RainHolder in rainHolders) {
            holder.updateRandom(rainDrawable, alpha)
            rainDrawable.draw(canvas)
        }
        return true
    }

    override fun setSize(width: Int, height: Int) {
        super.setSize(width, height)
        if (snowHolders.size == 0) {
            val minSize: Float = dp2px(MIN_SIZE)
            val maxSize: Float = dp2px(MAX_SIZE)
            val speed: Float = dp2px(200f) // 40当作中雪
            for (i in 0 until SNOW_COUNT) {
                val size: Float = BaseDrawer.Companion.getRandom(minSize, maxSize)
                val holder: SnowDrawer.SnowHolder = SnowDrawer.SnowHolder(
                    getRandom(0f, width.toFloat()),
                    size,
                    height.toFloat(),
                    speed
                )
                snowHolders.add(holder)
            }
        }
        if (rainHolders.size == 0) {
            val rainWidth: Float = dp2px(2f) //*(1f -  getDownRandFloat(0, 1));
            val minRainHeight: Float = dp2px(8f)
            val maxRainHeight: Float = dp2px(14f)
            val speed: Float = dp2px(360f)
            for (i in 0 until RAIN_COUNT) {
                val x: Float = BaseDrawer.Companion.getRandom(0f, width.toFloat())
                val holder: RainDrawer.RainHolder =
                    RainDrawer.RainHolder(
                        x, rainWidth, minRainHeight, maxRainHeight,
                        height.toFloat(), speed
                    )
                rainHolders.add(holder)
            }
        }
    }

    override fun getSkyBackgroundGradient(): IntArray {
        return if (isNight) SkyBackground.RAIN_N else SkyBackground.RAIN_D
    }

    companion object {
        val TAG: String = RainAndSnowDrawer::class.java.simpleName
        private val SNOW_COUNT: Int = 15
        private val RAIN_COUNT: Int = 30
        private val MIN_SIZE: Float = 6f // dp
        private val MAX_SIZE: Float = 14f // dp
    }
}