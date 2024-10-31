package com.theme.lambda.launcher.utils

import android.content.Intent
import android.provider.Settings


object SystemIntentUtil {
    fun getWifiSetPageIntent() = Intent(Settings.ACTION_WIFI_SETTINGS)
    fun getBLueToothSetPageIntent() = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
    fun getBatteryChargingSetPageIntent() = Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS)
    fun getTimeSetPageIntent() = Intent(Settings.ACTION_DATE_SETTINGS)
    fun getCellularSetPageIntent() = Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)
    fun getLightSetPageIntent() = Intent(Settings.ACTION_DISPLAY_SETTINGS)
    fun getRomSetPageIntent() = Intent(Settings.ACTION_INTERNAL_STORAGE_SETTINGS)
    fun getLocalSetPageIntent() = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
}

