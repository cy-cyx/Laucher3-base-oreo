package com.theme.lambda.launcher.data.model

import com.google.gson.annotations.SerializedName
import com.theme.lambda.launcher.appinfo.AppIconInfo
import com.theme.lambda.launcher.appinfo.AppInfo

class ManifestBean {

    @SerializedName("icons")
    var icons: ArrayList<IconBean> = arrayListOf()

    @SerializedName("background")
    var background: String = ""

    @SerializedName("widgets")
    var widgets: ArrayList<WidgetsBean> = arrayListOf()
}

class IconBean : Comparable<IconBean> {
    @SerializedName("pn")
    var pn: String = ""

    @SerializedName("icon")
    var icon: String = ""

    var appIconInfo: AppIconInfo? = null
    var AppInfo: AppInfo? = null
    var isInstall = false
    var isLock = true
    var isSelect = false

    override fun compareTo(other: IconBean): Int {
        if (isInstall && !other.isInstall) {
            return -1
        } else if (!isInstall && other.isInstall) {
            return 1
        } else {
            if (appIconInfo != null && other.appIconInfo == null) {
                return -1
            } else if (appIconInfo == null && other.appIconInfo != null) {
                return 1
            } else {
                return 0
            }

        }
    }
}


class WidgetsBean {
    @SerializedName("widget_type")
    var widgetType = ""

    @SerializedName("widget_large_preview")
    var widgetLargePreview = ""

    @SerializedName("widget_mid_preview")
    var widgetMidPreview = ""

    @SerializedName("widget_small_preview")
    var widgetSmallPreview = ""

    @SerializedName("widget_res_large")
    var widgetResLarge: ArrayList<WidgetResBean> = arrayListOf()

    @SerializedName("widget_res_mid")
    var widgetResMid: ArrayList<WidgetResBean> = arrayListOf()

    @SerializedName("widget_res_small")
    var widgetResSmall: ArrayList<WidgetResBean> = arrayListOf()
}

class WidgetResBean {
    @SerializedName("name")
    var name = ""

    @SerializedName("pic")
    var pic = ""

    @SerializedName("color")
    var color = ""

    @SerializedName("color1")
    var color1 = 0L

    @SerializedName("font_family")
    var fontFamily = ""

    @SerializedName("font_size")
    var fontSize = 30

    @SerializedName("clock_style")
    var clockStyle = ""

    @SerializedName("mask_alpha")
    var maskAlpha: Float = 0f

    @SerializedName("mask_color")
    var maskColor: String = ""

    @SerializedName("bgs")
    var bgs: String = ""

    @SerializedName("text")
    var text: String = ""

    @SerializedName("text_location")
    var textLocation: Int = 1

    @SerializedName("exchange_interval")
    var exchangeInterval = 0L

    @SerializedName("order")
    var order: Int = 0
}