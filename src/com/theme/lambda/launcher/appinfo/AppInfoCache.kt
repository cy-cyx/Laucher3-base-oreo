package com.theme.lambda.launcher.appinfo

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object AppInfoCache {

    val appInfos = ArrayList<AppInfo>()

    fun init(context: Context){
        GlobalScope.launch(Dispatchers.IO) {
            appInfos.addAll(AppInfoUtil.getAppInfo(context,true))
        }
    }

}