package com.lambdaweather.data.model

import com.google.gson.annotations.SerializedName

class RemoteConfigModel {
    @SerializedName("recall_cap")
    val recallCap: Long? = null

    @SerializedName("recall_notification")
    val recallNotification: Boolean? = null

    @SerializedName("immersion")
    val immersion: Boolean = false

    @SerializedName("enable_verbose_log")
    val enableVerboseLog: Boolean = false

    @SerializedName("ad_expire_in_ms")
    val adExpireInMs: Long? = null

    @SerializedName("ref_flags")
    val refFlags: Array<String>? = null

    @SerializedName("lockscreen_switch")
    val lockscreenSwitch: Boolean? = null

    @SerializedName("notification_cap")
    val notificationCap: Long? = null

    @SerializedName("location_cap")
    val locationCap: Long? = null

    @SerializedName("no_location_cap")
    val noLocationCap: Long? = null

    @SerializedName("valid_postback_duration_in_sec")
    val validPostbackDurationInSec: Long? = null
}