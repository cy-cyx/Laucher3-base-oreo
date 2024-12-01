package com.theme.lambda.launcher.appwidget.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetrics
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.launcher3.databinding.ItemCalendarBinding
import com.lambda.common.utils.CommonUtil
import com.lambda.common.utils.TimeUtil


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

    fun buildCalendarViewBitmap(
        w: Int,
        h: Int,
        fontSize: Int,
        color: Int,
        selectColor: Int
    ): Bitmap {
        val calendarView = CalendarView(CommonUtil.appContext!!, w, h, fontSize, color, selectColor)
        calendarView.measure(
            MeasureSpec.makeMeasureSpec(CommonUtil.dp2px(w.toFloat()), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(CommonUtil.dp2px(h.toFloat()), MeasureSpec.EXACTLY)
        )
        calendarView.layout(0, 0, CommonUtil.dp2px(w.toFloat()), CommonUtil.dp2px(h.toFloat()))
        calendarView.isDrawingCacheEnabled = true
        calendarView.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(calendarView.drawingCache)
        calendarView.destroyDrawingCache()
        return bitmap
    }

    class CalendarView(
        context: Context,
        w: Int,
        h: Int,
        fontSize: Int,
        color: Int,
        selectColor: Int
    ) :
        FrameLayout(context) {

        var data = ArrayList<String>()

        init {
            // 测试数据
            data.addAll(arrayListOf("S", "M", "T", "W", "T", "F", "S"))
            for (i in 1 until TimeUtil.getCurrentMonthStartWeek()) {
                data.add("")
            }
            for (i in 1..TimeUtil.getCurrentMonthLastDay()) {
                data.add(i.toString())
            }
            addView(RecyclerView(context).apply {
                layoutManager = GridLayoutManager(context, 7)
                adapter = CalendarAdapter(w, h, fontSize, color, selectColor, data)
            })
        }

        class CalendarAdapter(
            var w: Int,
            var h: Int,
            var fontSize: Int,
            var color: Int,
            var selectColor: Int,
            var data: ArrayList<String>
        ) :
            RecyclerView.Adapter<ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val view = ItemCalendarBinding.inflate(LayoutInflater.from(parent.context))
                view.textTv.setTextColor(color)
                view.textTv.gravity = Gravity.CENTER
                view.textTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize.toFloat())
                var i = data.size / 7
                if (data.size % 7 > 1) {
                    i++
                }
                view.root.layoutParams =
                    LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        CommonUtil.dp2px(h / i.toFloat())
                    )
                return CalendarViewHolder(view.root)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                if (holder is CalendarViewHolder) {
                    val bind = ItemCalendarBinding.bind(holder.view)

                    bind.textTv.setText(data[position])
                    if (data[position] == TimeUtil.getCurrentDate().toString()) {
                        bind.maskCm.visibility = View.VISIBLE
                        bind.maskCm.setColor(selectColor)
                    }
                }
            }

            override fun getItemCount(): Int {
                return data.size
            }

            class CalendarViewHolder(var view: View) : ViewHolder(view)
        }
    }
}