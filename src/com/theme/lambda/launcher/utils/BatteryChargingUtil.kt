package com.theme.lambda.launcher.utils

import android.content.Context
import android.os.BatteryManager
import com.android.launcher3.R
import com.lambda.common.utils.CommonUtil

object BatteryChargingUtil {

    var isCharging = false

    fun getPowerSize(): Int {
        val bm =
            (CommonUtil.appContext?.getSystemService(Context.BATTERY_SERVICE)) as? BatteryManager
        val property = bm?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        return property ?: 100
    }
}


fun Int.toBatteryChargingSrc(): Int {
    if (this < 15) {
        return R.drawable.ic_panel_low
    } else if (this > 85) {
        return R.drawable.ic_panel_full
    } else {
        return R.drawable.ic_panel_mid
    }
}

fun Int.toBatteryChargingKey(): String {
    if (this < 15) {
        return "power_low"
    } else if (this > 85) {
        return "power_full"
    } else {
        return "power_mid"
    }
}

fun Int.toBatteryChargingResBeanKey(): String {
    if (this < 15) {
        return "power_0"
    } else if (this in 16..49) {
        return "power_25"
    } else if (this in 50..85) {
        return "power_75"
    } else {
        return "power_100"
    }
}