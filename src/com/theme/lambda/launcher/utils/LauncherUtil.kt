package com.theme.lambda.launcher.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import com.theme.lambda.launcher.ui.permissionguide.HomeLauncherSetGuideActivity
import com.theme.lambda.launcher.ui.vivosetting.VivoSettingActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object LauncherUtil {

    var gotoSetting = false

    fun gotoSetLauncher(context: Context) {
        if (SystemUtil.getDeviceBrand() == SystemUtil.PHONE_VIVO) {
            VivoSettingActivity.start(context)
            return
        }

        gotoSetLauncherWithOutGuide(context)
    }

    fun gotoSetLauncherWithOutGuide(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_HOME_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            context.startActivity(intent)

            // 引导
//            GlobalScope.launch {
//                delay(300)
//                HomeLauncherSetGuideActivity.start(context)
//            }
            gotoSetting = true
        } catch (e: Exception) {
        }
    }

    fun isDefaultLauncher(context: Context): Boolean {
        var intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        var info = context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return info?.activityInfo?.packageName?.equals(context.packageName) ?: false
    }
}