package com.lambdaweather.view.dynamicweather

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable

/**
 * 晴天
 */
class SunnyDrawer constructor(context: Context) : BaseDrawer(context, false) {
    private val drawable: GradientDrawable =
        GradientDrawable(GradientDrawable.Orientation.BL_TR, intArrayOf(0x20ffffff, 0x10ffffff))
    private val holders: ArrayList<SunnyHolder> = ArrayList()

    //	private SunnyHolder holder;
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun drawWeather(canvas: Canvas, alpha: Float): Boolean {
        val size: Float = width * centerOfWidth
        for (holder: SunnyHolder in holders) {
            holder.updateRandom(drawable, alpha)
            drawable.draw(canvas)
        }
        paint.color = Color.argb((alpha * 0.18f * 255f).toInt(), 0xff, 0xff, 0xff)
        canvas.drawCircle(size, size, width * 0.12f, paint)
        return true
    }

    private val centerOfWidth: Float = 0.02f

    init {
        drawable.shape = GradientDrawable.OVAL
        drawable.gradientType = GradientDrawable.RADIAL_GRADIENT
        paint.color = 0x33ffffff
    }

    //private static final float SUNNY_MIN_SIZE = 60f;// dp
    //private static final float SUNNY_MAX_SIZE = 500f;// dp
    override fun setSize(width: Int, height: Int) {
        super.setSize(width, height)
        if (holders.size == 0) {
            val minSize: Float = width * 0.16f //dp2px(SUNNY_MIN_SIZE);
            val maxSize: Float = width * 1.5f //dp2px(SUNNY_MAX_SIZE);
            val center: Float = width * centerOfWidth
            val deltaSize: Float = (maxSize - minSize) / SUNNY_COUNT
            for (i in 0 until SUNNY_COUNT) {
                val curSize: Float =
                    maxSize - i * deltaSize * BaseDrawer.Companion.getRandom(0.9f, 1.1f)
                val holder: SunnyHolder = SunnyHolder(center, center, curSize, curSize)
                holders.add(holder)
            }
        }
        //		if(this.holder == null){
//			final float center = width * 0.25f;
//			final float size = width * 0.3f;
//			holder = new SunnyHolder(center, center, size, size);
//		}
    }

    class SunnyHolder constructor(var x: Float, var y: Float, var w: Float, var h: Float) {
        val maxAlpha: Float = 1f
        var curAlpha // [0,1]
                : Float
        var alphaIsGrowing: Boolean = true
        private val minAlpha: Float = 0.5f

        init {
            curAlpha = BaseDrawer.Companion.getRandom(minAlpha, maxAlpha)
        }

        fun updateRandom(drawable: GradientDrawable, alpha: Float) {
            // curAlpha += getRandom(-0.01f, 0.01f);
            // curAlpha = Math.max(0f, Math.min(maxAlpha, curAlpha));
            val delta: Float = BaseDrawer.Companion.getRandom(0.002f * maxAlpha, 0.005f * maxAlpha)
            if (alphaIsGrowing) {
                curAlpha += delta
                if (curAlpha > maxAlpha) {
                    curAlpha = maxAlpha
                    alphaIsGrowing = false
                }
            } else {
                curAlpha -= delta
                if (curAlpha < minAlpha) {
                    curAlpha = minAlpha
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
        val TAG: String = SunnyDrawer::class.java.simpleName
        private val SUNNY_COUNT: Int = 3
    }
}