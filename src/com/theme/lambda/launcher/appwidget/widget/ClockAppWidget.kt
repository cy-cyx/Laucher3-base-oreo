package com.theme.lambda.launcher.appwidget.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import com.android.launcher3.ThemeManager
import com.google.gson.reflect.TypeToken
import com.theme.lambda.launcher.appwidget.WidgetManager
import com.theme.lambda.launcher.appwidget.WidgetType
import com.theme.lambda.launcher.appwidget.builder.ClockWidgetBuilder
import com.theme.lambda.launcher.data.model.WidgetsBean
import com.lambda.common.utils.CommonUtil
import com.lambda.common.utils.getSpString
import com.lambda.common.utils.putSpString
import com.lambda.common.utils.GsonUtil
import com.lambda.common.utils.SpKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ClockAppWidget : AppWidgetProvider() {

    companion object {
        private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        private val xClockAppIds: ArrayList<Int> by lazy {
            val result = ArrayList<Int>()
            try {
                val keyRemoveOfferIdsSp = SpKey.clockWidgetIds.getSpString()
                val typeToken = object : TypeToken<List<Int>>() {}
                result.addAll(ArrayList(GsonUtil.gson.fromJson(keyRemoveOfferIdsSp, typeToken)))
            } catch (e: Exception) {
            }
            result
        }

        private fun addClockId(id: Int) {
            if (!xClockAppIds.contains(id)) {
                xClockAppIds.add(id)
                SpKey.clockWidgetIds.putSpString(GsonUtil.gson.toJson(xClockAppIds))
            }
        }

        private var lastUpDataTime = 0L

        fun upData() {
            if (System.currentTimeMillis() - lastUpDataTime < 10000) return
            lastUpDataTime = System.currentTimeMillis()

            ioScope.launch {
                xClockAppIds.forEach { id ->
                    ThemeManager.getThemeManagerIfExist()?.getCurManifest()?.widgets?.forEach {
                        if (it.widgetType == WidgetType.Clocks.type) {
                            val view = ClockWidgetBuilder().buildSmallWidget(
                                CommonUtil.appContext!!,
                                ThemeManager.getThemeManagerIfExist()?.showThemeId ?: "",
                                it
                            )
                            view?.let { v ->
                                WidgetManager.appWidgetManager.updateAppWidget(id, v)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        ioScope.launch {
            for (appWidgetId in appWidgetIds) {
                var bean: WidgetsBean? = null
                ThemeManager.getThemeManagerIfExist()?.getCurManifest()?.widgets?.forEach {
                    if (it.widgetType == WidgetType.Clocks.type) {
                        bean = it
                    }
                }
                val view = ClockWidgetBuilder().buildSmallWidget(
                    context,
                    ThemeManager.getThemeManagerIfExist()?.showThemeId ?: "",
                    bean
                )
                view?.let { v ->
                    appWidgetManager.updateAppWidget(appWidgetId, v)
                }
                addClockId(appWidgetId)
            }
        }
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
    }
}