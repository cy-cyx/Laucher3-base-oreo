package com.theme.lambda.launcher.widget

import android.appwidget.AppWidgetHostView
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewDebug.ExportedProperty
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RemoteViews
import com.android.launcher3.R
import com.android.launcher3.ThemeManager
import com.theme.lambda.launcher.statistics.EventName
import com.theme.lambda.launcher.statistics.EventUtil
import com.theme.lambda.launcher.ui.weather.ui.WeatherActivity
import com.theme.lambda.launcher.utils.WeatherTimerUtils

class WeatherWidgetHostView constructor(private val context: Context) : AppWidgetHostView(context) {
    @ExportedProperty(category = "launcher")
    private var mPreviousOrientation = 0
    private val remoteViews: RemoteViews by lazy {
        RemoteViews(appWidgetInfo.provider.packageName, 0)
    }

    override fun updateAppWidget(remoteViews: RemoteViews?) {
        // Store the orientation in which the widget was inflated
        mPreviousOrientation = resources.configuration.orientation
        super.updateAppWidget(remoteViews)
    }

    fun isReinflateRequired(): Boolean {
        // Re-inflate is required if the orientation has changed since last inflation.
        return mPreviousOrientation != resources.configuration.orientation
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        try {
            super.onLayout(changed, left, top, right, bottom)
        } catch (e: RuntimeException) {
            post { // Update the widget with 0 Layout id, to reset the view to error view.
                updateAppWidget(remoteViews)
            }
        }
    }

    override fun getErrorView(): View {
        return getDefaultView(this)
    }

    companion object {
        @JvmStatic
        fun getDefaultView(parent: ViewGroup): View {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_widget_weather, parent, false)
            v.findViewById<LinearLayout>(R.id.ll1).setOnClickListener {
                if (ThemeManager.getThemeManagerIfExist()?.isPreviewMode == true) return@setOnClickListener
                try {
                    parent.context.startActivity(
                        Intent(
                            AlarmClock.ACTION_SHOW_ALARMS
                        ).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                    )
                } catch (e: Exception) {
                    // 跳不过去就去时间设置页
                    parent.context.startActivity(
                        Intent(
                            Settings.ACTION_DATE_SETTINGS
                        ).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                    )
                }
            }
            v.findViewById<LinearLayout>(R.id.ll2).setOnClickListener {
                if (ThemeManager.getThemeManagerIfExist()?.isPreviewMode == true) return@setOnClickListener
                WeatherActivity.start(parent.context)
                EventUtil.logEvent(EventName.LWeather, Bundle().apply {
                    putString("type", "click")
                })
            }
            v.findViewById<ImageView>(R.id.iv_refresh).setOnClickListener {
                WeatherTimerUtils.getIpLocation()
                EventUtil.logEvent(EventName.LWeather, Bundle().apply {
                    putString("type", "refresh")
                })
            }
            return v
        }
    }
}