package com.theme.lambda.launcher.appwidget.builder

import android.content.Context
import android.widget.RemoteViews
import com.theme.lambda.launcher.data.model.WidgetsBean

interface BaseBuilder {

    suspend fun buildSmallWidget(
        context: Context,
        id: String,
        widgetsBean: WidgetsBean?
    ): RemoteViews?

    suspend fun buildMediumWidget(
        context: Context,
        id: String,
        widgetsBean: WidgetsBean?
    ): RemoteViews?

    suspend fun buildLargeWidget(
        context: Context,
        id: String,
        widgetsBean: WidgetsBean?
    ): RemoteViews?

}