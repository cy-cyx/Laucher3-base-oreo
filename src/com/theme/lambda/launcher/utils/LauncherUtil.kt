package com.theme.lambda.launcher.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

object LauncherUtil {

    fun gotoSetLauncher(context: Context) {
        val intent = Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setClassName(
            "android",
            "com.android.internal.app.ResolverActivity"
        );
        context.startActivity(intent)
    }

    fun isDefaultLauncher(context: Context): Boolean {
        var intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        var info = context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return info?.activityInfo?.packageName?.equals(context.packageName) ?: false
    }
}