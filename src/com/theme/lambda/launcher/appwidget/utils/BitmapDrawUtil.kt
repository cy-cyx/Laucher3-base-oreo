package com.theme.lambda.launcher.appwidget.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetrics
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.util.TypedValue
import android.widget.TextView
import com.theme.lambda.launcher.utils.CommonUtil


object BitmapDrawUtil {

    fun buildTextViewBitmap(
        text: String,
        textSize: Float,
        textColor: Int,
        typeface: Typeface
    ): Bitmap {
        val tv: TextView = TextView(CommonUtil.appContext).apply {
            this.text = text
            this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize)
            this.typeface = typeface
            this.setTextColor(textColor)
        }
        val paint = Paint()
        paint.textSize = tv.textSize
        paint.typeface = typeface
        val rect = Rect()
        paint.getTextBounds(text, 0, text.length, rect)
        val fontMetrics: FontMetrics = paint.fontMetrics
        val h = fontMetrics.bottom - fontMetrics.top
        val w = paint.measureText(text)
        tv.layout(0, 0, w.toInt() + 1, h.toInt() + 1)
        tv.isDrawingCacheEnabled = true
        tv.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(tv.drawingCache)
        tv.destroyDrawingCache()
        return bitmap
    }

    fun buildMaskBitmap(w: Int, h: Int, radius: Float, color: Int, alpha: Float): Bitmap {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = color
        paint.alpha = (alpha * 255).toInt()
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val rectF = RectF(0f, 0f, w.toFloat(), h.toFloat())
        canvas.drawRoundRect(
            rectF, CommonUtil.dp2px(radius).toFloat(),
            CommonUtil.dp2px(radius).toFloat(), paint
        )
        return bitmap
    }
}