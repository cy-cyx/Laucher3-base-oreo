package com.theme.lambda.launcher.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ColorTipView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var paint = Paint()

    private var viewWidth = 0
    private var viewHeight = 0

    private var color = Color.parseColor("#ff00ff")

    init {
        paint.isAntiAlias = true
    }

    fun setColor(c: Int) {
        color = c
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        viewHeight = MeasureSpec.getSize(heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = color

        val x = viewWidth / 2f
        canvas.drawCircle(x, x, x, paint)
    }
}