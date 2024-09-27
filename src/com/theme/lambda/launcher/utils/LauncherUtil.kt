package com.theme.lambda.launcher.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings

object LauncherUtil {

    fun gotoSetLauncher(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setClassName(
                "android",
                "com.android.internal.app.ResolverActivity"
            );
            context.startActivity(intent)
        } catch (e: Exception) {
            try {
                val intent: Intent = Intent(Settings.ACTION_HOME_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }catch (e:Exception){

            }
        }
    }

    fun isDefaultLauncher(context: Context): Boolean {
        var intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        var info = context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return info?.activityInfo?.packageName?.equals(context.packageName) ?: false
    }
}