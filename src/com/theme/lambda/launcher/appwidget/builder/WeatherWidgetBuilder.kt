package com.theme.lambda.launcher.appwidget.builder

import android.content.Context
import android.graphics.Color
import android.widget.RemoteViews
import com.android.launcher3.R
import com.theme.lambda.launcher.appwidget.utils.WeatherManager
import com.theme.lambda.launcher.data.model.WidgetsBean
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.GlideUtil

class WeatherWidgetBuilder : BaseBuilder {
    override suspend fun buildSmallWidget(
        context: Context,
        id: String,
        widgetsBean: WidgetsBean?
    ): RemoteViews? {
        val view = RemoteViews(context.packageName, R.layout.widget_weather_s)
        val bean = widgetsBean?.widgetResSmall
        val bgResBean = bean?.find { it.name == "bg" }
        bgResBean?.let {
            val filesDir = CommonUtil.appContext!!.filesDir.path
            val url = "$filesDir/wallpaper/${id}/${it.pic}"
            var bitmap = GlideUtil.loadBitmap(CommonUtil.appContext!!, url, 10f)
            view.setImageViewBitmap(R.id.bgIv, bitmap)
        }

        val colorResBean = bean?.find { it.name == "font_color" }
        var color = Color.parseColor("#ffffffff")
        try {
            color = Color.parseColor(colorResBean?.color)
        } catch (e: Exception) {
        }

        view.setTextViewText(R.id.cityTv, WeatherManager.getCountry())
        view.setTextColor(R.id.cityTv, color)
        view.setTextViewText(R.id.temperatureTv, WeatherManager.getTemperature())
        view.setTextColor(R.id.temperatureTv, color)
        view.setTextColor(R.id.duTv, color)
        view.setTextViewText(R.id.temperatureHLTv, WeatherManager.getTemperatureHL())
        view.setTextColor(R.id.temperatureHLTv, color)
        view.setTextViewText(R.id.weatherDesTv, WeatherManager.getWeatherDes())
        view.setTextColor(R.id.weatherDesTv, color)
        view.setImageViewResource(R.id.weatherIconIv, WeatherManager.getWeatherIcon())

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