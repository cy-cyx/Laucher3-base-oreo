package com.lambdaweather.view.dynamicweather

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import com.lambda.weather.LambdaWeather
import com.lambda.weather.R
import java.util.*

/**
 * 雷阵雨
 */
class RainAndThunder constructor(context: Context, isNight: Boolean) :
    BaseDrawer(context, isNight) {
    private val thunderDrawable: ThunderDrawable = ThunderDrawable(R.drawable.ima_lightning)
    private val rainDrawable: RainDrawer.RainDrawable = RainDrawer.RainDrawable()
    private val thunderHolders: ArrayList<ThunderHolder> = ArrayList<ThunderHolder>()
    private val rainHolders: ArrayList<RainDrawer.RainHolder> = ArrayList<RainDrawer.RainHolder>()

    override fun drawWeather(canvas: Canvas, alpha: Float): Boolean {
        for (holder: ThunderHolder in thunderHolders) {
            holder.updateRandom(thunderDrawable, alpha)
            thunderDrawable.draw(canvas)
        }
        for (holder: RainDrawer.RainHolder in rainHolders) {
            holder.updateRandom(rainDrawable, alpha)
            rainDrawable.draw(canvas)
        }
        return true
    }

    override fun setSize(width: Int, height: Int) {
        super.setSize(width, height)
        if (thunderHolders.size == 0) {
            val minSize: Float = dp2px(MIN_SIZE)
            val maxSize: Float = dp2px(MAX_SIZE)
            for (i in 0 until THUNDER_COUNT) {
                val size: Float = BaseDrawer.Companion.getRandom(minSize, maxSize)
                val holder: ThunderHolder = ThunderHolder(
                    width, height
                )
                thunderHolders.add(holder)
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

    class ThunderDrawable(val resId: Int) {
        var x: Float = 0f
        var y: Float = 0f
        var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val mThunderBitmap: Bitmap by lazy {
            BitmapFactory.decodeResource(LambdaWeather.application.resources, resId)
        }

        init {
            paint.style = Paint.Style.STROKE

        }

        fun setColor(color: Int) {
            paint.color = color
        }

        fun setStrokeWidth(strokeWidth: Float) {
            paint.strokeWidth = strokeWidth
        }

        fun setLocation(x: Float, y: Float) {
            this.x = x
            this.y = y
        }

        fun setAlpha(alpha: Int) {
            paint.alpha = alpha
        }

        fun draw(canvas: Canvas) {
            canvas.drawBitmap(mThunderBitmap, x + mThunderBitmap.width / 2, y, paint)
        }
    }

    class ThunderHolder constructor(
        var imgWidth: Int,
        var imgHeight: Int,
    ) {
        var x: Float
        var y: Float
        var mAlpha: Float
        var mCount: Int = 0

        init {
            x = (Random().nextDouble() * 0.5 - 1 / 3 * imgWidth).toFloat()
            y = (Random().nextDouble() * -0.05).toFloat()
            mAlpha = 1f
        }

        fun updateRandom(drawable: ThunderDrawable, alpha: Float) {

            drawable.setAlpha((255 * mAlpha).toInt())
            mAlpha = (mAlpha - 0.01).toFloat()
            if (mAlpha < 0) {
                mAlpha = 0f
                mCount++
            }
            if (mCount == 300) {
                mAlpha = 1f
                mCount = 0
            }

        }
    }

    override fun getSkyBackgroundGradient(): IntArray {
        return if (isNight) SkyBackground.RAIN_N else SkyBackground.RAIN_D
    }

    companion object {
        val TAG: String = RainAndThunder::class.java.simpleName
        private val THUNDER_COUNT: Int = 1
        private val RAIN_COUNT: Int = 30
        private val MIN_SIZE: Float = 6f // dp
        private val MAX_SIZE: Float = 14f // dp
    }
}