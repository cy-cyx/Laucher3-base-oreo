package com.theme.lambda.launcher.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import com.theme.lambda.launcher.recall.RecallManager
import com.theme.lambda.launcher.ui.vivosetting.VivoSettingActivity

object LauncherUtil {

    // 去launcher开启权限的标志位
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
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            context.startActivity(intent)

            gotoSetting = true
        } catch (e: Exception) {
        }
        RecallManager.startTimeoutRecall(context)
    }

    fun isDefaultLauncher(context: Context): Boolean {
        var intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        var info = context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return info?.activityInfo?.packageName?.equals(context.packageName) ?: false
    }
}