package com.theme.lambda.launcher.appwidget.builder

import android.content.Context
import android.view.View
import android.widget.RemoteViews
import com.android.launcher3.R
import com.theme.lambda.launcher.data.model.WidgetsBean
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.GlideUtil

class ClockWidgetBuilder : BaseBuilder {
    override suspend fun buildSmallWidget(
        context: Context,
        id: String,
        widgetsBean: WidgetsBean?
    ): RemoteViews? {
        val view = RemoteViews(context.packageName, R.layout.widget_clock_s)
        val bean = widgetsBean?.widgetResSmall
        val bgResBean = bean?.find { it.name == "bg" }
        bgResBean?.let {
            val filesDir = CommonUtil.appContext!!.filesDir.path
            val url = "$filesDir/wallpaper/${id}/${it.pic}"
            var bitmap = GlideUtil.loadBitmap(CommonUtil.appContext!!, url, 10f)
            view.setImageViewBitmap(R.id.bgIv, bitmap)
        }

        val styleResBean = bean?.find { it.name == "clock_style" }
        view.setViewVisibility(R.id.clock3Ac, View.GONE)
        when (styleResBean?.clockStyle) {
            "arabic" -> view.setViewVisibility(R.id.clock1Ac, View.VISIBLE)
            "roman" -> view.setViewVisibility(R.id.clock2Ac, View.VISIBLE)
            else -> view.setViewVisibility(R.id.clock3Ac, View.VISIBLE)
        }

        return view
    }

    override suspend fun buildMediumWidget(
        context: Context,
        id: String,
        widgetsBean: WidgetsBean?
    ): RemoteViews? {
       return null
    }

    override suspend fun buildLargeWidget(
        context: Context,
        id: String,
        widgetsBean: WidgetsBean?
    ): RemoteViews? {
        return null
    }
}