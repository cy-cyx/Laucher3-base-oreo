package com.lambdaweather.view.dynamicweather

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.util.Log

/**
 * 晴天的晚上 （星空）
 *
 * @author Mixiaoxiao
 */
class StarDrawer constructor(context: Context) : BaseDrawer(context, true) {
    private val drawable: GradientDrawable
    private val holders: ArrayList<StarHolder> = ArrayList()

    init {
        drawable =
            GradientDrawable(GradientDrawable.Orientation.BL_TR, intArrayOf(-0x1, 0x00ffffff))
        drawable.shape = GradientDrawable.OVAL
        drawable.gradientType = GradientDrawable.RADIAL_GRADIENT
        drawable.gradientRadius = (Math.sqrt(2.0) * 60).toFloat()
    }

    override fun drawWeather(canvas: Canvas, alpha: Float): Boolean {
        for (holder: StarHolder in holders) {
            holder.updateRandom(drawable, alpha)
            // drawable.setBounds(0, 0, 360, 360);
            // drawable.setGradientRadius(360/2.2f);//测试出来2.2比较逼真
            try {
                drawable.draw(canvas)
            } catch (e: Exception) {
                // TODO Auto-generated catch block
                e.printStackTrace()
                Log.e("FUCK", "drawable.draw(canvas)->" + drawable.bounds.toShortString())
            }
        }
        return true
    }

    override fun setSize(width: Int, height: Int) {
        super.setSize(width, height)
        if (holders.size == 0) {
            val starMinSize: Float = dp2px(STAR_MIN_SIZE)
            val starMaxSize: Float = dp2px(STAR_MAX_SIZE)
            for (i in 0 until STAR_COUNT) {
                val starSize: Float = BaseDrawer.Companion.getRandom(starMinSize, starMaxSize)
                val y: Float = BaseDrawer.Companion.getDownRandFloat(0f, height.toFloat())
                // 20%的上半部分屏幕最高alpha为1，其余的越靠下最高alpha越小
                val maxAlpha: Float = 0.2f + 0.8f * (1f - y / height)
                val holder: StarHolder = StarHolder(
                    BaseDrawer.Companion.getRandom(0f, width.toFloat()),
                    y,
                    starSize,
                    starSize,
                    maxAlpha
                )
                holders.add(holder)
            }
            // holders.add(new StarHolder(360, 360, 200, 200));
        }
    }

    override fun getSkyBackgroundGradient(): IntArray {
        return SkyBackground.CLEAR_N
    }

    class StarHolder constructor(
        var x: Float, var y: Float, var w: Float, var h: Float, // [0,1]
        val maxAlpha: Float
    ) {
        var curAlpha // [0,1]
                : Float
        var alphaIsGrowing: Boolean = true

        init {
            curAlpha = BaseDrawer.Companion.getRandom(0f, maxAlpha)
        }

        fun updateRandom(drawable: GradientDrawable, alpha: Float) {
            // curAlpha += getRandom(-0.01f, 0.01f);
            // curAlpha = Math.max(0f, Math.min(maxAlpha, curAlpha));
            val delta: Float = BaseDrawer.Companion.getRandom(0.003f * maxAlpha, 0.012f * maxAlpha)
            if (alphaIsGrowing) {
                curAlpha += delta
                if (curAlpha > maxAlpha) {
                    curAlpha = maxAlpha
                    alphaIsGrowing = false
                }
            } else {
                curAlpha -= delta
                if (curAlpha < 0) {
                    curAlpha = 0f
                    alphaIsGrowing = true
                }
            }
            val left: Int = Math.round(x - w / 2f)
            val right: Int = Math.round(x + w / 2f)
            val top: Int = Math.round(y - h / 2f)
            val bottom: Int = Math.round(y + h / 2f)
            drawable.setBounds(left, top, right, bottom)
            drawable.gradientRadius = w / 2.2f
            drawable.alpha = (255 * curAlpha * alpha).toInt()
        }
    }

    companion object {
        val TAG: String = StarDrawer::class.java.simpleName
        private val STAR_COUNT: Int = 80
        private val STAR_MIN_SIZE: Float = 2f // dp
        private val STAR_MAX_SIZE: Float = 6f // dp
    }
}