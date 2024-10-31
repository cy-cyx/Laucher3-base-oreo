package com.theme.lambda.launcher.appwidget.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CalendarMaskView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var w = 0
    private var h = 0
    private var maskColor = Color.WHITE
    private var paint = Paint()

    fun setColor(color: Int) {
        maskColor = color
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        w = MeasureSpec.getSize(widthMeasureSpec)
        h = MeasureSpec.getSize(heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var radius = Math.min(w, h)
        paint.isAntiAlias = true
        paint.setColor(maskColor)
        canvas.drawCircle(w / 2f, h / 2f + 0.5f, radius / 2f, paint)
    }
}