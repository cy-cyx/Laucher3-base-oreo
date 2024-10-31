package com.theme.lambda.launcher.appwidget.builder

import android.content.Context
import android.graphics.Color
import android.widget.RemoteViews
import com.android.launcher3.R
import com.theme.lambda.launcher.appwidget.utils.BitmapDrawUtil
import com.theme.lambda.launcher.data.model.WidgetsBean
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.GlideUtil
import com.theme.lambda.launcher.utils.TimeUtil

class CalendarWidgetBuilder : BaseBuilder {
    override suspend fun buildSmallWidget(
        context: Context,
        id: String,
        widgetsBean: WidgetsBean?
    ): RemoteViews? {
       return null
    }

    override suspend fun buildMediumWidget(
        context: Context,
        id: String,
        widgetsBean: WidgetsBean?
    ): RemoteViews? {
        val view = RemoteViews(context.packageName, R.layout.widget_calendar_m)

        val filesDir = CommonUtil.appContext!!.filesDir.path
        var bean = widgetsBean

        val widgetResBean = bean?.widgetResMid?.find { it.name == "bg" }
        widgetResBean?.let {
            val url = "$filesDir/wallpaper/${id}/${it.pic}"
            var bitmap = GlideUtil.loadBitmap(CommonUtil.appContext!!, url, 10f)
            view.setImageViewBitmap(R.id.bgIv, bitmap)
        }

        val colorResBean = bean?.widgetResSmall?.find { it.name == "font_color" }
        var color = Color.parseColor("#ff000000")
        try {
            color = Color.parseColor(colorResBean?.color)
        } catch (e: Exception) {
        }
        val selectColorResBean = bean?.widgetResSmall?.find { it.name == "select_color" }
        var selectColor = Color.parseColor("#FFCA447A")
        try {
            selectColor = Color.parseColor(selectColorResBean?.color)
        } catch (e: Exception) {
        }

        view.setTextViewText(R.id.monthTv, TimeUtil.getMouth())
        view.setTextColor(R.id.monthTv, color)

        view.setTextViewText(R.id.dayTv, TimeUtil.getCurrentDate().toString())
        view.setTextColor(R.id.dayTv, color)

        val bitmap = BitmapDrawUtil.buildCalendarViewBitmap(200, 160, 12, color, selectColor)
        view.setImageViewBitmap(R.id.calenderIv, bitmap)

        return view
    }

    override suspend fun buildLargeWidget(
        context: Context,
        id: String,
        widgetsBean: WidgetsBean?
    ): RemoteViews? {
        return null
    }
}