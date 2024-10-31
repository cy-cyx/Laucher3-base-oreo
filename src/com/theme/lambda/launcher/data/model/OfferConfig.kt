package com.theme.lambda.launcher.data.model

import com.google.gson.annotations.SerializedName

class OfferConfig {

    @SerializedName("offers")
    var offers: ArrayList<Offers> = arrayListOf()

    @SerializedName("is_show_in_box")
    var isShowInBox: Boolean = true
}

class Offers {
    @SerializedName("id")
    var id: String = ""

    @SerializedName("name")
    var name: String = ""

    @SerializedName("icon_url")
    var iconUrl: String = ""

    // 本地iconUrl下载后的链接
    @SerializedName("local_icon_url")
    var localIconUrl = ""

    @SerializedName("imp_url")
    var impUrl: String = ""

    @SerializedName("click_url")
    var clickUrl: String = ""

    @SerializedName("pn")
    var pn: String = ""

    @SerializedName("is_removable")
    var isRemovable: Boolean = true
}