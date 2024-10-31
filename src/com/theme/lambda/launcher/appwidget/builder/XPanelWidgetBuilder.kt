package com.theme.lambda.launcher.appwidget.builder

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.graphics.Color
import android.widget.RemoteViews
import com.android.launcher3.R
import com.theme.lambda.launcher.appwidget.utils.BitmapDrawUtil
import com.theme.lambda.launcher.data.model.WidgetsBean
import com.theme.lambda.launcher.netstate.NetStateUtil
import com.theme.lambda.launcher.utils.BatteryChargingUtil
import com.theme.lambda.launcher.utils.BluetoothUtil
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.DisplayUtil
import com.theme.lambda.launcher.utils.GlideUtil
import com.theme.lambda.launcher.utils.RomUtil
import com.theme.lambda.launcher.utils.SystemIntentUtil
import com.theme.lambda.launcher.utils.TimeUtil
import com.theme.lambda.launcher.utils.toBatteryChargingKey
import com.theme.lambda.launcher.utils.toBatteryChargingSrc
import com.theme.lambda.launcher.utils.toRomKey
import com.theme.lambda.launcher.utils.toRomSrc

class XPanelWidgetBuilder : BaseBuilder {

    override suspend fun buildSmallWidget(
        context: Context,
        id: String,
        widgetsBean: WidgetsBean
    ): RemoteViews? {
        return null
    }

    override suspend fun buildMediumWidget(
        context: Context,
        id: String,
        widgetsBean: WidgetsBean
    ): RemoteViews? {
        val view = RemoteViews(context.packageName, R.layout.widget_x_panel_m)

        val filesDir = CommonUtil.appContext!!.filesDir.path
        var bean = widgetsBean

        val widgetResBean = bean.widgetResMid.find { it.name == "bg" }
        widgetResBean?.let {
            val url = "$filesDir/wallpaper/${id}/${it.pic}"
            var bitmap = GlideUtil.loadBitmap(CommonUtil.appContext!!, url, 10f)
            view.setImageViewBitmap(R.id.bgIv, bitmap)
        }

        val colorResBean = bean.widgetResMid.find { it.name == "font_color" }
        var color = Color.parseColor("#ffffffff")
        try {
            color = Color.parseColor(colorResBean?.color)
        } catch (e: Exception) {
        }

        val maskAlphaResBean = bean.widgetResSmall.find { it.name == "mask_alpha" }
        var maskAlpha = maskAlphaResBean?.maskAlpha ?: 0.4f
        val maskColorResBean = bean.widgetResSmall.find { it.name == "mask_color" }
        var maskColor = Color.parseColor("#00000000")
        try {
            maskColor = Color.parseColor(maskColorResBean?.maskColor)
        } catch (e: Exception) {
        }
        val bitmap1 =
            BitmapDrawUtil.buildMaskBitmap(530, 150, 10f, maskColor, maskAlpha)
        val bitmap2 =
            BitmapDrawUtil.buildMaskBitmap(150, 150, 10f, maskColor, maskAlpha)
        val bitmap3 =
            BitmapDrawUtil.buildMaskBitmap(100, 150, 10f, maskColor, maskAlpha)
        val bitmap4 =
            BitmapDrawUtil.buildMaskBitmap(280, 150, 10f, maskColor, maskAlpha)

        view.setImageViewBitmap(R.id.bg1Iv, bitmap1)
        view.setImageViewBitmap(R.id.bg2Iv, bitmap2)
        view.setImageViewBitmap(R.id.bg3Iv, bitmap3)
        view.setImageViewBitmap(R.id.bg4Iv, bitmap2)
        view.setImageViewBitmap(R.id.bg5Iv, bitmap2)
        view.setImageViewBitmap(R.id.bg6Iv, bitmap2)
        view.setImageViewBitmap(R.id.bg7Iv, bitmap4)

        // 时间
        view.setTextViewText(R.id.weekTv, TimeUtil.getWeek())
        view.setTextViewText(R.id.dataTv, TimeUtil.getData())
        view.setTextColor(R.id.timeTv, color)
        view.setTextColor(R.id.weekTv, color)
        view.setTextColor(R.id.dataTv, color)

        view.setOnClickPendingIntent(
            R.id.timeLl,
            PendingIntent.getActivity(
                context,
                0,
                SystemIntentUtil.getTimeSetPageIntent(),
                FLAG_IMMUTABLE
            )
        )

        // 电量
        val batteryCharging = BatteryChargingUtil.getPowerSize()
        view.setTextViewText(R.id.powerTv, "$batteryCharging%")
        view.setTextColor(R.id.powerTv, color)

        val batteryResBean =
            bean.widgetResMid.find { it.name == batteryCharging.toBatteryChargingKey() }
        if (batteryResBean?.pic != null) {
            val url = "$filesDir/wallpaper/${id}/${batteryResBean.pic}"
            var bitmap = GlideUtil.loadBitmap(CommonUtil.appContext!!, url, w = 100, h = 100)
            view.setImageViewBitmap(R.id.powerIv, bitmap)
        } else {
            view.setImageViewResource(R.id.powerIv, batteryCharging.toBatteryChargingSrc())
        }

        view.setOnClickPendingIntent(
            R.id.powerFl,
            PendingIntent.getActivity(
                context,
                0,
                SystemIntentUtil.getBatteryChargingSetPageIntent(),
                FLAG_IMMUTABLE
            )
        )

        // 蓝牙
        var isBlueToothOpen = BluetoothUtil.isEnabled()
        if (isBlueToothOpen) {
            val bluetoothBean = bean.widgetResMid.find { it.name == "bluetooth_on" }
            if (bluetoothBean?.pic != null) {
                val url = "$filesDir/wallpaper/${id}/${bluetoothBean.pic}"
                var bitmap = GlideUtil.loadBitmap(CommonUtil.appContext!!, url, w = 100, h = 100)
                view.setImageViewBitmap(R.id.bluetoothIv, bitmap)
            } else {
                view.setImageViewResource(R.id.bluetoothIv, R.drawable.ic_panel_bluetooth)
            }
        } else {
            val bluetoothBean = bean.widgetResMid.find { it.name == "bluetooth_off" }
            if (bluetoothBean?.pic != null) {
                val url = "$filesDir/wallpaper/${id}/${bluetoothBean.pic}"
                var bitmap = GlideUtil.loadBitmap(CommonUtil.appContext!!, url, w = 100, h = 100)
                view.setImageViewBitmap(R.id.bluetoothIv, bitmap)
            } else {
                view.setImageViewResource(R.id.bluetoothIv, R.drawable.ic_panel_bluetooth_off)
            }
        }
        view.setOnClickPendingIntent(
            R.id.bluetoothFl,
            PendingIntent.getActivity(
                context, 0,
                SystemIntentUtil.getBLueToothSetPageIntent(), FLAG_IMMUTABLE
            )
        )

        // wifi
        var isWifiOpen = NetStateUtil.isWifiConnected(CommonUtil.appContext)
        if (isWifiOpen) {
            val wifiBean = bean.widgetResMid.find { it.name == "wifi_on" }
            if (wifiBean?.pic != null) {
                val url = "$filesDir/wallpaper/${id}/${wifiBean.pic}"
                var bitmap = GlideUtil.loadBitmap(CommonUtil.appContext!!, url, w = 100, h = 100)
                view.setImageViewBitmap(R.id.wifiIv, bitmap)
            } else {
                view.setImageViewResource(R.id.wifiIv, R.drawable.ic_panel_wifi)
            }
        } else {
            val wifiBean = bean.widgetResMid.find { it.name == "wifi_off" }
            if (wifiBean?.pic != null) {
                val url = "$filesDir/wallpaper/${id}/${wifiBean.pic}"
                var bitmap = GlideUtil.loadBitmap(CommonUtil.appContext!!, url, w = 100, h = 100)
                view.setImageViewBitmap(R.id.wifiIv, bitmap)
            } else {
                view.setImageViewResource(R.id.wifiIv, R.drawable.ic_panel_wifi_off)
            }
        }
        view.setOnClickPendingIntent(
            R.id.wifiFl,
            PendingIntent.getActivity(
                context, 0,
                SystemIntentUtil.getWifiSetPageIntent(), FLAG_IMMUTABLE
            )
        )

        // cellular
        var isCellularOpen = NetStateUtil.isMobileConnected(CommonUtil.appContext)
        if (isCellularOpen) {
            val networkBean = bean.widgetResMid.find { it.name == "network_on" }
            if (networkBean?.pic != null) {
                val url = "$filesDir/wallpaper/${id}/${networkBean.pic}"
                var bitmap = GlideUtil.loadBitmap(CommonUtil.appContext!!, url, w = 100, h = 100)
                view.setImageViewBitmap(R.id.cellularIv, bitmap)
            } else {
                view.setImageViewResource(R.id.cellularIv, R.drawable.ic_panel_cellular)
            }
        } else {
            val networkBean = bean.widgetResMid.find { it.name == "network_off" }
            if (networkBean?.pic != null) {
                val url = "$filesDir/wallpaper/${id}/${networkBean.pic}"
                var bitmap = GlideUtil.loadBitmap(CommonUtil.appContext!!, url, w = 100, h = 100)
                view.setImageViewBitmap(R.id.cellularIv, bitmap)
            } else {
                view.setImageViewResource(R.id.cellularIv, R.drawable.ic_panel_cellular_off)
            }
        }
        view.setOnClickPendingIntent(
            R.id.cellularFl,
            PendingIntent.getActivity(
                context, 0,
                SystemIntentUtil.getCellularSetPageIntent(), FLAG_IMMUTABLE
            )
        )

        // 亮度
        var lightRate = (DisplayUtil.getScreenBrightness(context) / 255.0 * 100).toInt()
        view.setTextViewText(R.id.lightTv, "$lightRate%")
        view.setTextColor(R.id.lightTv, color)

        val lightResBean = bean.widgetResMid.find { it.name == "light" }
        if (lightResBean?.pic != null) {
            val url = "$filesDir/wallpaper/${id}/${lightResBean.pic}"
            var bitmap = GlideUtil.loadBitmap(CommonUtil.appContext!!, url, w = 100, h = 100)
            view.setImageViewBitmap(R.id.lightIv, bitmap)
        }

        view.setOnClickPendingIntent(
            R.id.lightLl,
            PendingIntent.getActivity(
                context, 0,
                SystemIntentUtil.getLightSetPageIntent(), FLAG_IMMUTABLE
            )
        )

        // rom
        var emoryRate = (RomUtil.getAvailMemory(context) / RomUtil.getTotalMemory(context)
            .toFloat() * 100).toInt()
        view.setTextViewText(R.id.romTv, "$emoryRate%")
        view.setTextColor(R.id.romTv, color)
        view.setTextColor(R.id.ronTitleTv, color)

        val romResBean = bean.widgetResMid.find { it.name == emoryRate.toRomKey() }
        if (romResBean?.pic != null) {
            val url = "$filesDir/wallpaper/${id}/${romResBean.pic}"
            var bitmap = GlideUtil.loadBitmap(CommonUtil.appContext!!, url, w = 100, h = 100)
            view.setImageViewBitmap(R.id.romIv, bitmap)
        } else {
            view.setImageViewResource(R.id.romIv, emoryRate.toRomSrc())
        }

        view.setOnClickPendingIntent(
            R.id.romFl,
            PendingIntent.getActivity(
                context, 0,
                SystemIntentUtil.getRomSetPageIntent(), FLAG_IMMUTABLE
            )
        )

        return view
    }

    override suspend fun buildLargeWidget(
        context: Context,
        id: String,
        widgetsBean: WidgetsBean
    ): RemoteViews? {
        return null
    }
}