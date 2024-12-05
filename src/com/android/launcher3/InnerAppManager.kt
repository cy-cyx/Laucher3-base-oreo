package com.android.launcher3

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.lambda.common.utils.CommonUtil.appContext
import com.lambda.news.ui.home.NewsHomeActivity
import com.lambda.news.ui.home.NewsHomeFragment

object InnerAppManager {

    @JvmStatic
    var actionHost = "InnerApp:"  // 用来拼包名和intent

    @JvmStatic
    var InnerNewsAction = "${actionHost}New"

    @JvmStatic
    fun addToAllAppsList(allAppsList: AllAppsList) {
        allAppsList.add(AppInfo().apply {
            intent = Intent(InnerNewsAction).apply {
                setComponent(ComponentName(InnerNewsAction, InnerNewsAction))
            }
            title = appContext!!.getString(R.string.news);
            componentName = ComponentName(InnerNewsAction, InnerNewsAction)
            contentDescription = ""
        }, null)
    }

    @JvmStatic
    fun isInnerApp(shortcutInfo: ShortcutInfo): Boolean {
        return shortcutInfo.getIntent().action?.contains(actionHost) ?: false
    }

    @JvmStatic
    fun isInnerApp(appInfo: AppInfo): Boolean {
        return appInfo.getIntent().action?.contains(actionHost) ?: false
    }

    @JvmStatic
    fun clickInnerApp(context: Context, shortcutInfo: ShortcutInfo) {
        val action = shortcutInfo.getIntent().action ?: ""
        click(context, action)
    }

    @JvmStatic
    fun clickInnerApp(context: Context, appInfo: AppInfo) {
        val action = appInfo.getIntent().action ?: ""
        click(context, action)
    }

    private fun click(context: Context, action: String) {
        when (action) {
            InnerNewsAction -> {
                NewsHomeActivity.start(context, NewsHomeActivity.sFromHome)
            }
        }
        NewInstallationManager.clickApp(action)
    }
}