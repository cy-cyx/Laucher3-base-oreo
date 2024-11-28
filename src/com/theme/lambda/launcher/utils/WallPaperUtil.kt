package com.theme.lambda.launcher.utils

import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Build
import androidx.annotation.WorkerThread
import com.lambda.common.utils.CommonUtil
import java.io.File


object WallPaperUtil {

    @WorkerThread
    fun setHomeScreen(file: String) {
        if (!File(file).exists()) {
            return
        }
        val bitmap = BitmapFactory.decodeFile(file)
        val manager =
            CommonUtil.appContext?.getSystemService(Context.WALLPAPER_SERVICE) as? WallpaperManager
        if (Build.VERSION.SDK_INT >= 24) {
            manager?.setBitmap(
                bitmap,
                Rect(0, 0, bitmap.width, bitmap.height),
                false,
                WallpaperManager.FLAG_SYSTEM
            )
        } else {
            manager?.setBitmap(bitmap)
        }
    }

    @WorkerThread
    fun setLockScreen(file: String) {
        if (!File(file).exists()) {
            return
        }
        val bitmap = BitmapFactory.decodeFile(file)
        val manager =
            CommonUtil.appContext?.getSystemService(Context.WALLPAPER_SERVICE) as? WallpaperManager
        if (Build.VERSION.SDK_INT >= 24) {
            manager?.setBitmap(
                bitmap,
                Rect(0, 0, bitmap.width, bitmap.height),
                false,
                WallpaperManager.FLAG_LOCK
            )
        } else {
            manager?.setBitmap(bitmap)
        }
    }

    @WorkerThread
    fun setHomeAndLockScreen(file: String) {
        if (!File(file).exists()) {
            return
        }
        val manager =
            CommonUtil.appContext?.getSystemService(Context.WALLPAPER_SERVICE) as? WallpaperManager
        val bitmap = BitmapFactory.decodeFile(file)

        try {
            if (Build.VERSION.SDK_INT >= 24) {
                manager?.setBitmap(
                    bitmap,
                    Rect(0, 0, bitmap.width, bitmap.height),
                    false
                )
            } else {
                manager?.setBitmap(bitmap)
            }
        } catch (e: Exception) {
            // nodo
        }

    }
}