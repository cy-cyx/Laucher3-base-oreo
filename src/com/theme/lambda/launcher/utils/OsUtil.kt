package com.theme.lambda.launcher.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Process


object OsUtil {
    fun getProcessName(cxt: Context, pid: Int): String? {
        //获取ActivityManager对象
        val am: ActivityManager = cxt.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        //在运行的进程的
        val runningApps: List<ActivityManager.RunningAppProcessInfo> = am.getRunningAppProcesses()
            ?: return null
        for (procInfo in runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName
            }
        }
        return null
    }

    fun getCurProcessName(context: Context): String? {
        val pid = Process.myPid()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (activityManager.runningAppProcesses == null) return null
        for (appProcess in activityManager
            .runningAppProcesses) {
            if (appProcess.pid == pid) {
                return appProcess.processName
            }
        }
        return null
    }

}