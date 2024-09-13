package com.theme.lambda.launcher.widget

import android.appwidget.AppWidgetHostView
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewDebug.ExportedProperty
import android.view.ViewGroup
import android.widget.RemoteViews
import com.android.launcher3.R
import com.theme.lambda.launcher.ui.weather.WeatherActivity

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
                .inflate(R.layout.view_home_weather_widget, parent, false)
            v.setOnClickListener {
                parent.context.startActivity(
                    Intent(
                        parent.context,
                        WeatherActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }
            return v
        }
    }
}