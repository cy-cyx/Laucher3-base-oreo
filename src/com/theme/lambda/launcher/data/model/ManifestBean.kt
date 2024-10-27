package com.theme.lambda.launcher.data.model

import com.google.gson.annotations.SerializedName
import com.theme.lambda.launcher.appinfo.AppIconInfo
import com.theme.lambda.launcher.appinfo.AppInfo

class ManifestBean {

    @SerializedName("icons")
    var icons: ArrayList<IconBean> = arrayListOf()

    @SerializedName("background")
    var background: String = ""
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