package com.lambdaweather.view.dynamicweather

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import com.android.launcher3.R
import com.lambdaweather.LambdaWeather
import java.util.Random

abstract class BaseDrawer(protected var context: Context, isNight: Boolean) {
    enum class Type {
        DEFAULT, CLEAR_D, CLEAR_N, RAIN_D, RAIN_N, SNOW_D,
        SNOW_N, CLOUDY_D, CLOUDY_N, OVERCAST_D, OVERCAST_N,
        FOG_D, FOG_N, HAZE_D, HAZE_N, SAND_D, SAND_N, WIND_D,
        WIND_N, RAIN_SNOW_D, RAIN_SNOW_N, RAIN_THUNDER_D, RAIN_THUNDER_N, UNKNOWN_D, UNKNOWN_N
    }

    object SkyBackground {
        val BLACK = intArrayOf(
            ContextCompat.getColor(LambdaWeather.application, R.color.color_010c39),
            ContextCompat.getColor(LambdaWeather.application, R.color.color_525876)
        )

        //		public static final int[] CLEAR_D = new int[] { 0xff3d99c2, 0xff4f9ec5 };
        //		public static final int[] CLEAR_N = new int[] { 0xff0d1229, 0xff262c42 };
        val CLEAR_D = intArrayOf(
            ContextCompat.getColor(LambdaWeather.application, R.color.color_0084ff),
            ContextCompat.getColor(LambdaWeather.application, R.color.color_6ab6ff)
        )
        val CLEAR_N = intArrayOf(
            ContextCompat.getColor(LambdaWeather.application, R.color.color_010c39),
            ContextCompat.getColor(LambdaWeather.application, R.color.color_525876)
        )

        // ////////////
        val OVERCAST_D = intArrayOf(
            ContextCompat.getColor(LambdaWeather.application, R.color.color_525876),
            ContextCompat.getColor(LambdaWeather.application, R.color.color_b0c0cf)
        )
        val OVERCAST_N = intArrayOf(
            ContextCompat.getColor(LambdaWeather.application, R.color.color_010c39),
            ContextCompat.getColor(LambdaWeather.application, R.color.color_525876)
        )

        // ////////////
        val RAIN_D = intArrayOf(
            ContextCompat.getColor(LambdaWeather.application, R.color.color_525876),
            ContextCompat.getColor(LambdaWeather.application, R.color.color_b0c0cf)
        )
        val RAIN_N = intArrayOf(
            ContextCompat.getColor(LambdaWeather.application, R.color.color_010c39),
            ContextCompat.getColor(LambdaWeather.application, R.color.color_525876)
        )

        // ////////////
        val FOG_D = intArrayOf(
            ContextCompat.getColor(LambdaWeather.application, R.color.color_525876),
            ContextCompat.getColor(LambdaWeather.application, R.color.color_b0c0cf)
        )
        val FOG_N = intArrayOf(
            ContextCompat.getColor(LambdaWeather.application, R.color.color_010c39),
            ContextCompat.getColor(LambdaWeather.application, R.color.color_525876)
        )

        // ////////////
        val SNOW_D = intArrayOf(
            ContextCompat.getColor(LambdaWeather.application, R.color.color_525876),
            ContextCompat.getColor(LambdaWeather.application, R.color.color_b0c0cf)
        )
        val SNOW_N = intArrayOf(
            ContextCompat.getColor(LambdaWeather.application, R.color.color_010c39),
            ContextCompat.getColor(LambdaWeather.application, R.color.color_525876)
        )

        // ////////////
        val CLOUDY_D = intArrayOf(
            ContextCompat.getColor(LambdaWeather.application, R.color.color_0084ff),
            ContextCompat.getColor(LambdaWeather.application, R.color.color_6ab6ff)
        )
        val CLOUDY_N = intArrayOf(
            ContextCompat.getColor(LambdaWeather.application, R.color.color_010c39),
            ContextCompat.getColor(LambdaWeather.application, R.color.color_525876)
        )

        // ////////////
        val HAZE_D = intArrayOf(
            ContextCompat.getColor(LambdaWeather.application, R.color.color_525876),
            ContextCompat.getColor(LambdaWeather.application, R.color.color_b0c0cf)
        )
        val HAZE_N = intArrayOf(
            ContextCompat.getColor(LambdaWeather.application, R.color.color_010c39),
            ContextCompat.getColor(LambdaWeather.application, R.color.color_525876)
        )

        // ////////////
        val SAND_D = intArrayOf(
            ContextCompat.getColor(LambdaWeather.application, R.color.color_525876),
            ContextCompat.getColor(LambdaWeather.application, R.color.color_b0c0cf)
        )
        val SAND_N = intArrayOf(
            ContextCompat.getColor(LambdaWeather.application, R.color.color_010c39),
            ContextCompat.getColor(LambdaWeather.application, R.color.color_525876)
        )
    }

    //	private float curPercent = 0f;
    private val desity: Float
    protected var width = 0
    protected var height = 0
    private var skyDrawable: GradientDrawable? = null
    protected val isNight: Boolean

    init {
        desity = context.resources.displayMetrics.density
        this.isNight = isNight
    }

    protected fun reset() {}
    private fun makeSkyBackground(): GradientDrawable {
        return GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, getSkyBackgroundGradient())
    }

    private fun drawSkyBackground(canvas: Canvas?, alpha: Float) {
        if (skyDrawable == null) {
            skyDrawable = makeSkyBackground()
            skyDrawable!!.setBounds(0, 0, width, height)
        }
        skyDrawable!!.alpha = Math.round(alpha * 255f)
        skyDrawable!!.draw(canvas!!)
    }

    /**
     * @param canvas
     * @return needDrawNextFrame
     */
    fun draw(canvas: Canvas, alpha: Float): Boolean {
        drawSkyBackground(canvas, alpha)
        //long start = AnimationUtils.currentAnimationTimeMillis();
        //		if (needDrawNextFrame) {
//			curPercent += getFrameOffsetPercent();
//			if (curPercent > 1) {
//				curPercent = 0f;
//			}
//		}
        // Log.i(TAG, getClass().getSimpleName() + " drawWeather: "
        // + (AnimationUtils.currentAnimationTimeMillis() - start) + "ms");
        return drawWeather(canvas, alpha)
    }

    abstract fun drawWeather(canvas: Canvas, alpha: Float): Boolean // return

    // needDrawNextFrame
    open fun getSkyBackgroundGradient(): IntArray {
        return if (isNight) SkyBackground.CLEAR_N else SkyBackground.CLEAR_D
    }


    open fun setSize(width: Int, height: Int) {
        if (this.width != width && this.height != height) {
            this.width = width
            this.height = height
            if (skyDrawable != null) {
                skyDrawable!!.setBounds(0, 0, width, height)
            }
        }
    }

    // 每一帧的百分比
    protected val frameOffsetPercent: Float
        get() =// 每一帧的百分比
            1f / 40f

    fun dp2px(dp: Float): Float {
        return dp * desity
    }

    companion object {
        fun makeDrawerByType(context: Context, type: Type?): BaseDrawer {
            return when (type) {
                Type.CLEAR_D -> SunnyDrawer(
                    context
                )
                Type.CLEAR_N -> StarDrawer(
                    context
                )
                Type.RAIN_D -> RainDrawer(
                    context,
                    false
                )
                Type.RAIN_N -> RainDrawer(
                    context,
                    true
                )
                Type.SNOW_D -> SnowDrawer(
                    context,
                    false
                )
                Type.SNOW_N -> SnowDrawer(
                    context,
                    true
                )
                Type.CLOUDY_D -> CloudyDrawer(
                    context,
                    false
                )
                Type.CLOUDY_N -> CloudyDrawer(
                    context,
                    true
                )
                Type.OVERCAST_D -> OvercastDrawer(
                    context,
                    false
                )
                Type.OVERCAST_N -> OvercastDrawer(
                    context,
                    true
                )
                Type.FOG_D -> FogDrawer(
                    context,
                    false
                )
                Type.FOG_N -> FogDrawer(
                    context,
                    true
                )
                Type.HAZE_D -> HazeDrawer(
                    context,
                    false
                )
                Type.HAZE_N -> HazeDrawer(
                    context,
                    true
                )
                Type.SAND_D -> SandDrawer(
                    context,
                    false
                )
                Type.SAND_N -> SandDrawer(
                    context,
                    true
                )
                Type.WIND_D -> WindDrawer(
                    context,
                    false
                )
                Type.WIND_N -> WindDrawer(
                    context,
                    true
                )
                Type.RAIN_SNOW_D -> RainAndSnowDrawer(
                    context,
                    false
                )
                Type.RAIN_SNOW_N -> RainAndSnowDrawer(
                    context,
                    true
                )
                Type.RAIN_THUNDER_D -> RainAndThunder(
                    context,
                    false
                )
                Type.RAIN_THUNDER_N -> RainAndThunder(
                    context,
                    true
                )
                Type.UNKNOWN_D -> UnknownDrawer(
                    context,
                    false
                )
                Type.UNKNOWN_N -> UnknownDrawer(
                    context,
                    true
                )
                Type.DEFAULT -> DefaultDrawer(
                    context
                )
                else -> DefaultDrawer(context)
            }
        }

        val TAG = BaseDrawer::class.java.simpleName

        //	protected float getCurPercent() {
        //		return curPercent;
        //	}
        fun convertAlphaColor(percent: Float, originalColor: Int): Int {
            val newAlpha = (percent * 255).toInt() and 0xFF
            return newAlpha shl 24 or (originalColor and 0xFFFFFF)
        }

        // 获得0--n之内的不等概率随机整数，0概率最大，1次之，以此递减，n最小
        fun getAnyRandInt(n: Int): Int {
            val max = n + 1
            val bigend = (1 + max) * max / 2
            val rd = Random()
            val x = Math.abs(rd.nextInt() % bigend)
            var sum = 0
            for (i in 0 until max) {
                sum += max - i
                if (sum > x) {
                    return i
                }
            }
            return 0
        }

        /**
         * 获取[min, max)内的随机数，越大的数概率越小
         * 参考http://blog.csdn.net/loomman/article/details/3861240
         *
         * @param min
         * @param max
         * @return
         */
        fun getDownRandFloat(min: Float, max: Float): Float {
            val bigend = (min + max) * max / 2f
            // Random rd = new Random();
            val x = getRandom(min, bigend) // Math.abs(rd.nextInt() % bigend);
            var sum = 0
            var i = 0
            while (i < max) {
                sum += (max - i).toInt()
                if (sum > x) {
                    return i.toFloat()
                }
                i++
            }
            return min
        }

        /**
         * [min, max)
         *
         * @param min
         * @param max
         * @return
         */
        fun getRandom(min: Float, max: Float): Float {
            require(max >= min) {
                "max should bigger than min!!!!"
                // return 0f;
            }
            return (min + Math.random() * (max - min)).toFloat()
        }

        /**
         * 必须取[0,1]之间的float
         *
         * @param alpha
         * @return
         */
        fun fixAlpha(alpha: Float): Float {
            if (alpha > 1f) {
                return 1f
            }
            return if (alpha < 0f) {
                0f
            } else alpha
        }
    }
}