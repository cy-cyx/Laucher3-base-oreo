package com.theme.lambda.launcher.appwidget.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import com.android.launcher3.ThemeManager
import com.google.gson.reflect.TypeToken
import com.theme.lambda.launcher.appwidget.WidgetManager
import com.theme.lambda.launcher.appwidget.WidgetType
import com.theme.lambda.launcher.appwidget.builder.XPanelWidgetBuilder
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

class XPanelAppWidget : AppWidgetProvider() {

    companion object {
        private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        private val xPanelAppIds: ArrayList<Int> by lazy {
            val result = ArrayList<Int>()
            try {
                val keyRemoveOfferIdsSp = SpKey.xpanelWidgetIds.getSpString()
                val typeToken = object : TypeToken<List<Int>>() {}
                result.addAll(ArrayList(GsonUtil.gson.fromJson(keyRemoveOfferIdsSp, typeToken)))
            } catch (e: Exception) {
            }
            result
        }

        private fun addXPanelId(id: Int) {
            if (!xPanelAppIds.contains(id)) {
                xPanelAppIds.add(id)
                SpKey.xpanelWidgetIds.putSpString(GsonUtil.gson.toJson(xPanelAppIds))
            }
        }

        private var lastUpDataTime = 0L

        fun upData() {
            if (System.currentTimeMillis() - lastUpDataTime < 10000) return
            lastUpDataTime = System.currentTimeMillis()

            ioScope.launch {
                xPanelAppIds.forEach { id ->
                    ThemeManager.getThemeManagerIfExist()?.getCurManifest()?.widgets?.forEach {
                        if (it.widgetType == WidgetType.XPanel.type) {
                            val view = XPanelWidgetBuilder().buildMediumWidget(
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
                    if (it.widgetType == WidgetType.XPanel.type) {
                        bean = it
                    }
                }
                val view = XPanelWidgetBuilder().buildMediumWidget(
                    context,
                    ThemeManager.getThemeManagerIfExist()?.showThemeId ?: "",
                    bean
                )
                view?.let { v ->
                    appWidgetManager.updateAppWidget(appWidgetId, v)
                }
                addXPanelId(appWidgetId)
            }
        }
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
    }
}