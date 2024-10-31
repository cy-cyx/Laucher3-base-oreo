package com.theme.lambda.launcher.appwidget

import android.appwidget.AppWidgetManager
import com.theme.lambda.launcher.utils.CommonUtil

object WidgetManager {

    val appWidgetManager = AppWidgetManager.getInstance(CommonUtil.appContext)

    fun upData() {
        XPanelAppWidget.upData()
    }
}