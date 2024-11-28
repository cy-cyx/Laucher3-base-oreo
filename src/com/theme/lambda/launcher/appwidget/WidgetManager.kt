package com.theme.lambda.launcher.appwidget

import android.appwidget.AppWidgetManager
import com.theme.lambda.launcher.appwidget.utils.WeatherManager
import com.theme.lambda.launcher.appwidget.widget.CalendarAppWidget
import com.theme.lambda.launcher.appwidget.widget.ClockAppWidget
import com.theme.lambda.launcher.appwidget.widget.XPanelAppWidget
import com.lambda.common.utils.CommonUtil

object WidgetManager {

    val appWidgetManager: AppWidgetManager = AppWidgetManager.getInstance(CommonUtil.appContext)

    fun upData() {
        XPanelAppWidget.upData()
        ClockAppWidget.upData()
        CalendarAppWidget.upData()
        WeatherManager.init()
    }
}