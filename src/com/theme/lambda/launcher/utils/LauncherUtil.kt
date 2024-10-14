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

    fun gotoSetLauncher(context: Context) {
        if (SystemUtil.getDeviceBrand() == SystemUtil.PHONE_VIVO) {
            VivoSettingActivity.start(context)
            return
        }

        try {
            val intent: Intent = Intent(Settings.ACTION_HOME_SETTINGS)
            if (context is Activity) {
                context.startActivityForResult(intent, 0)
            } else {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }

            // 引导
            GlobalScope.launch {
                delay(300)
                HomeLauncherSetGuideActivity.start(context)
            }
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