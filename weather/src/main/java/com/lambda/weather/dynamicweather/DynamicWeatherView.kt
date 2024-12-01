package com.lambdaweather.view.dynamicweather

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Handler
import android.os.HandlerThread
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.lambdaweather.utils.TimeUtils
import com.lambdaweather.utils.gone
import com.lambdaweather.utils.visible

class DynamicWeatherView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs,defStyleAttr) {
    companion object {
        private const val TAG = "DynamicWeatherView"
    }

    private val TAG = "DynamicWeatherView"
    private var preDrawer: BaseDrawer? = null
    private lateinit var curDrawer: BaseDrawer
    private var curDrawerAlpha = 0f
    private var curType = BaseDrawer.Type.DEFAULT
    private var mWidth = 0
    private var mHeight = 0
    private lateinit var mDrawHandler: Handler
    private val mHandlerThread = HandlerThread("draw" + TimeUtils.getCurrentTimeSecond())
    private var mDrawFlag = false
    private var mStop = false

    init {
        init()
    }

    private fun init() {
        curDrawerAlpha = 0f
        mHandlerThread.start()
        mDrawHandler = Handler(mHandlerThread.looper)
    }

    private fun setDrawer(baseDrawer: BaseDrawer) {
        curDrawerAlpha = 0f
        if (::curDrawer.isInitialized) {
            preDrawer = curDrawer
        }
        curDrawer = baseDrawer
    }

    fun setDrawerType(type: BaseDrawer.Type) {
        if (type != curType) {
            curType = type
            setDrawer(BaseDrawer.makeDrawerByType(context, curType))
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val canvas: Canvas? = canvas
        if (canvas != null) {
            try {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                drawSurface(canvas)
            } catch (e: Exception) {
                Log.e(TAG, "Error drawing surface: ${e.message}")
                e.printStackTrace()
            } finally {
                if (!mStop) {
                    invalidate()
                }
            }
        }
    }

    private fun drawSurface(canvas: Canvas): Boolean {
        val w = mWidth
        val h = mHeight
        if (w == 0 || h == 0) {
            return true
        }
        var needDrawNextFrame = false
        if (::curDrawer.isInitialized) {
            curDrawer.setSize(w, h)
            needDrawNextFrame = curDrawer.draw(canvas, curDrawerAlpha)
        }
        if (preDrawer != null && curDrawerAlpha < 1f) {
            needDrawNextFrame = true
            preDrawer?.setSize(w, h)
            preDrawer?.draw(canvas, 1f - curDrawerAlpha)
        }
        if (curDrawerAlpha < 1f) {
            curDrawerAlpha += 0.04f
            if (curDrawerAlpha > 1) {
                curDrawerAlpha = 1f
                preDrawer = null
            }
        }
        return needDrawNextFrame
    }

    fun onResume() {
        Log.i(TAG, "onResume")
        this.visible()
        mDrawFlag = true
    }

    fun onPause() {
        Log.i(TAG, "onPause")
        this.gone()
        mDrawFlag = false
    }

    fun onDestroy() {
        Log.i(TAG, "onDestroy")
        this.gone()
        mDrawFlag = false
        mStop = true
        mHandlerThread.quitSafely()
    }

//    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
//        Log.i(TAG, "surfaceCreated")
//        mDrawHandler.post(mDrawRunnable)
//    }
//
//    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}
//
//    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
//        Log.i(TAG, "surfaceDestroyed")
//        mDrawFlag = false
//        mDrawHandler.removeCallbacks(mDrawRunnable)
//        return true
//    }
//
//    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
}