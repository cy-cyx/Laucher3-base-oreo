package com.lambdaweather.view.dynamicweather

import android.content.Context
import android.graphics.*

/**
 * 下雨
 */
class RainDrawer constructor(context: Context, isNight: Boolean) : BaseDrawer(context, isNight) {
    class RainDrawable {
        var x: Float = 0f
        var y: Float = 0f
        var length: Float = 0f
        var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

        init {
            paint.style = Paint.Style.STROKE
            //			paint.setStrokeJoin(Paint.Join.ROUND);
//			paint.setStrokeCap(Paint.Cap.ROUND);
        }

        fun setColor(color: Int) {
            paint.color = color
        }

        fun setStrokeWidth(strokeWidth: Float) {
            paint.strokeWidth = strokeWidth
        }

        fun setLocation(x: Float, y: Float, length: Float) {
            this.x = x
            this.y = y
            this.length = length
        }

        fun draw(canvas: Canvas) {
            canvas.drawLine(x, y - length, x, y, paint)
        }
    }

    enum class RainLevel {
        LIGHT, MEDIUM, HEAVY, VERY_HEAVY
    }

    private val drawable: RainDrawable
    private val holders: ArrayList<RainHolder> = ArrayList()
    private val cfg_count: Int = 50
    override fun drawWeather(canvas: Canvas, alpha: Float): Boolean {
        for (holder: RainHolder in holders) {
            holder.updateRandom(drawable, alpha)
            drawable.draw(canvas)
        }
        return true
    }

    override fun setSize(width: Int, height: Int) {
        super.setSize(width, height)
        if (holders.size == 0) {
//			Log.i(TAG, "x->" + x);
            val rainWidth: Float = dp2px(2f) //*(1f -  getDownRandFloat(0, 1));
            val minRainHeight: Float = dp2px(8f)
            val maxRainHeight: Float = dp2px(14f)
            val speed: Float = dp2px(400f)
            for (i in 0 until cfg_count) {
                val x: Float = BaseDrawer.Companion.getRandom(0f, width.toFloat())
                val holder: RainHolder =
                    RainHolder(x, rainWidth, minRainHeight, maxRainHeight, height.toFloat(), speed)
                holders.add(holder)
            }
        }
    }

    override fun getSkyBackgroundGradient(): IntArray {
        return if (isNight) SkyBackground.RAIN_N else SkyBackground.RAIN_D
    }

    //	private final float cfg_rainWidth ,cfg_minRainHeight,cfg_maxRainHeight,cfg_speed;
    init {
        drawable = RainDrawable()
        //		switch (level) {
//		case LIGHT:
//			cfg_count = 30;
//			cfg_minRainHeight = 
//			break;
//
//		default:
//			break;
//		}
    }

    class RainHolder constructor(
        var x: Float, //		public float y;//y 表示雨滴底部的y坐标,由curTime求得
        val rainWidth: Float, minRainHeight: Float, maxRainHeight: Float, maxY: Float, speed: Float
    ) {
        val rainHeight: Float
        val maxY // [0,1]
                : Float
        var curTime // [0,1]
                : Float
        val rainColor: Int
        val v //速度
                : Float
        //		public boolean alphaIsGrowing = true;
        /**
         * @param x 雨滴中心的x坐标
         * @param rainWidth 雨滴宽度
         * @param maxRainHeight  最大的雨滴长度
         * @param maxY 屏幕高度
         */
        init {
            rainHeight = BaseDrawer.Companion.getRandom(minRainHeight, maxRainHeight)
            rainColor = Color.argb(
                (BaseDrawer.Companion.getRandom(0.1f, 0.5f) * 255f).toInt(),
                0xff,
                0xff,
                0xff
            )
            this.maxY = maxY
            //			this.v0 = 0;//maxY * 0.1f;
            v = speed * BaseDrawer.Companion.getRandom(0.9f, 1.1f)
            val maxTime: Float =
                maxY / v //  (float) Math.sqrt(2f * maxY / acceleration);//s = 0.5*a*t^2;
            curTime = BaseDrawer.Companion.getRandom(0f, maxTime)
        }

        fun updateRandom(drawable: RainDrawable, alpha: Float) {
            curTime += 0.025f
            //			float curY = v0 * curTime + 0.5f * acceleration * curTime * curTime;
            val curY: Float = curTime * v
            if ((curY - rainHeight) > maxY) {
                curTime = 0f
            }
            drawable.setColor(
                Color.argb(
                    (Color.alpha(rainColor) * alpha).toInt(),
                    0xff,
                    0xff,
                    0xff
                )
            )
            drawable.setStrokeWidth(rainWidth)
            drawable.setLocation(x, curY, rainHeight)
        }
    }

    companion object {
        val TAG: String = RainDrawer::class.java.simpleName
        val acceleration: Float = 9.8f
    }
}