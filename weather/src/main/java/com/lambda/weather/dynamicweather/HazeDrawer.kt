package com.lambdaweather.view.dynamicweather

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.util.Log

/**
 * 霾
 */
class HazeDrawer constructor(context: Context, isNight: Boolean) : BaseDrawer(context, isNight) {
    private val drawable: GradientDrawable
    private val holders: ArrayList<HazeHolder> = ArrayList()
    private val minDX: Float
    private val maxDX: Float
    private val minDY: Float
    private val maxDY: Float

    init {
        drawable = GradientDrawable(
            GradientDrawable.Orientation.BL_TR,
            if (isNight) intArrayOf(0x55d4ba3f, 0x22d4ba3f) else intArrayOf(-0x77335999, 0x33cca667)
        ) //d4ba3f
        drawable.shape = GradientDrawable.OVAL
        drawable.gradientType = GradientDrawable.RADIAL_GRADIENT
        //		drawable.setGradientRadius((float)(Math.sqrt(2) * 60));  
        minDX = 0.04f
        maxDX = 0.065f //dp2px(1.5f);
        minDY = -0.02f //-dp2px(0.5f);
        maxDY = 0.02f //dp2px(0.5f);
    }

    override fun drawWeather(canvas: Canvas, alpha: Float): Boolean {
        for (holder: HazeHolder in holders) {
            holder.updateRandom(
                drawable,
                minDX,
                maxDX,
                minDY,
                maxDY,
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                alpha
            )
            //				drawable.setBounds(0, 0, 360, 360);
//				drawable.setGradientRadius(360/2.2f);//测试出来2.2比较逼真
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
            val minSize: Float = dp2px(0.8f)
            val maxSize: Float = dp2px(4.4f)
            for (i in 0..79) {
                val starSize: Float = getRandom(minSize, maxSize)
                val holder: HazeHolder = HazeHolder(
                    getRandom(0f, width.toFloat()),
                    getDownRandFloat(0f, height.toFloat()),
                    starSize,
                    starSize
                )
                holders.add(holder)
            }
            //			holders.add(new StarHolder(360, 360, 200, 200));
        }
    }

    class HazeHolder constructor(var x: Float, var y: Float, var w: Float, var h: Float) {
        fun updateRandom(
            drawable: GradientDrawable,
            minDX: Float,
            maxDX: Float,
            minDY: Float,
            maxDY: Float,
            minX: Float,
            minY: Float,
            maxX: Float,
            maxY: Float,
            alpha: Float
        ) {
            //alpha 还没用
            if (maxDX < minDX || (maxDY < minDY)) {
                throw IllegalArgumentException("max should bigger than min!!!!")
            }
            x += (getRandom(minDX, maxDX) * w)
            y += (getRandom(minDY, maxDY) * h)
            //			this.x = Math.min(maxX, Math.max(this.x, minX));
//			this.y = Math.min(maxY, Math.max(this.y, minY));
            if (x > maxX) {
                x = minX
            } else if (x < minX) {
                x = maxX
            }
            if (y > maxY) {
                y = minY
            } else if (y < minY) {
                y = maxY
            }
            val left: Int = Math.round(x - w / 2f)
            val right: Int = Math.round(x + w / 2f)
            val top: Int = Math.round(y - h / 2f)
            val bottom: Int = Math.round(y + h / 2f)
            drawable.alpha = (255f * alpha).toInt()
            drawable.setBounds(left, top, right, bottom)
            drawable.gradientRadius = w / 2.2f
        }
    }

    override fun getSkyBackgroundGradient(): IntArray {
        return if (isNight) SkyBackground.HAZE_N else SkyBackground.HAZE_D
    }
}