package com.lambdaweather.view.dynamicweather

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class SandDrawer constructor(context: Context, isNight: Boolean) : BaseDrawer(context, isNight) {
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val count: Int = 30
    private val holders: ArrayList<ArcHolder> = ArrayList()

    init {
        paint.style = Paint.Style.STROKE
    }

    override fun setSize(width: Int, height: Int) {
        super.setSize(width, height)
        if (holders.size == 0) {
            val cx: Float = -width * 0.3f
            val cy: Float = -width * 1.5f
            for (i in 0 until count) {
                val radiusWidth: Float = getRandom(width * 1.3f, width * 3.0f)
                val radiusHeight: Float = radiusWidth * getRandom(
                    0.92f,
                    0.96f
                ) //getRandom(width * 0.02f,  width * 1.6f);
                val strokeWidth: Float = dp2px(getDownRandFloat(1f, 2.5f))
                val sizeDegree: Float = getDownRandFloat(8f, 15f)
                holders.add(
                    ArcHolder(
                        cx,
                        cy,
                        radiusWidth,
                        radiusHeight,
                        strokeWidth,
                        30f,
                        99f,
                        sizeDegree,
                        if (isNight) -0x665a6faa else -0x445a6faa
                    )
                )
            }
        }
    }

    override fun drawWeather(canvas: Canvas, alpha: Float): Boolean {
        for (holder: ArcHolder in holders) {
            holder.updateAndDraw(canvas, paint, alpha)
        }
        return true
    }

    class ArcHolder constructor(
        private val cx: Float,
        private val cy: Float,
        private val radiusWidth: Float,
        private val radiusHeight: Float,
        private val strokeWidth: Float,
        private val fromDegree: Float,
        private val endDegree: Float,
        private val sizeDegree: Float,
        private val color: Int
    ) {
        private var curDegree: Float
        private val stepDegree: Float
        private val rectF: RectF = RectF()

        init {
            curDegree = getRandom(fromDegree, endDegree)
            stepDegree = getRandom(0.4f, 0.8f)
        }

        fun updateAndDraw(canvas: Canvas, paint: Paint, alpha: Float) {
            paint.color = convertAlphaColor(
                alpha * (Color.alpha(
                    color
                ) / 255f), color
            )
            paint.strokeWidth = strokeWidth
            curDegree += stepDegree * getRandom(0.8f, 1.2f)
            if (curDegree > (endDegree - sizeDegree)) {
                curDegree = fromDegree - sizeDegree
            }
            val startAngle: Float = curDegree
            val sweepAngle: Float = sizeDegree
            rectF.left = cx - radiusWidth
            rectF.top = cy - radiusHeight
            rectF.right = cx + radiusWidth
            rectF.bottom = cy + radiusHeight
            canvas.drawArc(rectF, startAngle, sweepAngle, false, paint)
        }
    }

    override fun getSkyBackgroundGradient(): IntArray {
        return if (isNight) SkyBackground.SAND_N else SkyBackground.SAND_D
    }
}