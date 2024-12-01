package com.lambdaweather.view.dynamicweather

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import com.lambda.weather.LambdaWeather
import com.lambda.weather.R

class CloudyDrawer(context: Context, isNight: Boolean) : BaseDrawer(context, isNight) {

    val holders: ArrayList<CloudHolder> = ArrayList()
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun setSize(width: Int, height: Int) {
        super.setSize(width, height)
        if (holders.size == 0) {
            holders.add(
                CloudHolder(width, height)
            )
        }
    }

    override fun drawWeather(canvas: Canvas, alpha: Float): Boolean {
        for (holder: CloudHolder in holders) {
            holder.updateAndDraw(canvas, paint, alpha)
        }
        return true
    }

    override fun getSkyBackgroundGradient(): IntArray {
        return if (isNight) SkyBackground.CLOUDY_N else SkyBackground.CLOUDY_D
    }

    class CircleHolder(
        private val cx: Float,
        private val cy: Float,
        private val dx: Float,
        private val dy: Float,
        private val radius: Float,
        private val percentSpeed: Float,
        private val color: Int
    ) {
        private var isGrowing: Boolean = true
        private var curPercent: Float = 0f
        fun updateAndDraw(canvas: Canvas, paint: Paint, alpha: Float) {
            val randomPercentSpeed: Float =
                getRandom(percentSpeed * 0.7f, percentSpeed * 1.3f)
            if (isGrowing) {
                curPercent += randomPercentSpeed
                if (curPercent > 1f) {
                    curPercent = 1f
                    isGrowing = false
                }
            } else {
                curPercent -= randomPercentSpeed
                if (curPercent < 0f) {
                    curPercent = 0f
                    isGrowing = true
                }
            }
            val curCX: Float = cx + dx * curPercent
            val curCY: Float = cy + dy * curPercent
            val curColor: Int = convertAlphaColor(
                alpha * (Color.alpha(
                    color
                ) / 255f), color
            )
            paint.color = curColor
            canvas.drawCircle(curCX, curCY, radius, paint)
        }
    }

    class CloudHolder(
        var imgWidth: Int,
        var imgHeight: Int,
    ) {

        private val mBitmap: Bitmap by lazy {
            BitmapFactory.decodeResource(LambdaWeather.application.resources, R.drawable.cloudy_ima_03)
        }

        private var mScale = 1f
        private var mAlpha = 1f
        private var mPost = 1f
        private var mdirection = false

        init {

        }

        fun updateAndDraw(canvas: Canvas, paint: Paint, alpha: Float) {
            val matrix = Matrix()
//            matrix.postScale(mScale,mScale,
//                (mBitmap.width / 2).toFloat(), (mBitmap.height / 2).toFloat()
//            )
            matrix.postTranslate(
                (mBitmap.width / 2 - mScale * 500).toFloat(),
                (mBitmap.height + 100).toFloat()
            )
            canvas.drawBitmap(mBitmap, matrix, paint)
            paint.alpha = (255 * mAlpha).toInt()
            if (mdirection) {
                mScale -= 0.001f
                mAlpha += 0.001f
                if (mScale <= 1.0f) {
                    mdirection = false
                }
            } else {
                mScale += 0.001f
                mAlpha -= 0.001f
                if (mScale >= 1.5f) {
                    mdirection = true
                }
            }
        }
    }
}