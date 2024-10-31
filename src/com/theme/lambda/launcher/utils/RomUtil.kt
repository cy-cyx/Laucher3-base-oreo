package com.theme.lambda.launcher.utils

import android.content.Context
import android.os.Environment
import android.os.StatFs
import com.android.launcher3.R


object RomUtil {

    var statFs = StatFs(Environment.getExternalStorageDirectory().path)

    fun getAvailMemory(context: Context): Long {
        return statFs.availableBytes
    }

    fun getTotalMemory(context: Context?): Long {
        return statFs.totalBytes
    }
}

fun Int.toRomSrc(): Int {
    if (this < 25) {
        return R.drawable.ic_panel_rom_25
    } else if (this < 50) {
        return R.drawable.ic_panel_rom_50
    } else if (this < 75) {
        return R.drawable.ic_panel_rom_75
    } else {
        return R.drawable.ic_panel_rom_100
    }
}

fun Int.toRomKey(): String {
    if (this < 25) {
        return "progress25"
    } else if (this < 50) {
        return "progress50"
    } else if (this < 75) {
        return "progress80"
    } else {
        return "progress100"
    }
}