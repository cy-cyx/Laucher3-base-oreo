package com.theme.lambda.launcher.data.model

import com.google.gson.annotations.SerializedName

class OfferConfig {

    @SerializedName("offers")
    var offers: ArrayList<Offers> = arrayListOf()

    @SerializedName("is_show_in_box")
    var isShowInBox: Boolean = true

    override fun hashCode(): Int {
        var i = 0
        offers.forEach {
            i += it.hashCode()
        }
        return i
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OfferConfig

        if (offers != other.offers) return false
        if (isShowInBox != other.isShowInBox) return false

        return true
    }
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

    @SerializedName("max_click")
    var maxClick: Int = 0

    override fun hashCode(): Int {
        return id.length * 1 + name.length * 2 + iconUrl.length * 3 /*+ localIconUrl.length * 4 */ +
                impUrl.length * 5 + clickUrl.length * 6 + pn.length * 7 + isRemovable.toString().length * 8 +
                maxClick * 9
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Offers

        if (id != other.id) return false
        if (name != other.name) return false
        if (iconUrl != other.iconUrl) return false
        if (localIconUrl != other.localIconUrl) return false
        if (impUrl != other.impUrl) return false
        if (clickUrl != other.clickUrl) return false
        if (pn != other.pn) return false
        if (isRemovable != other.isRemovable) return false

        return true
    }
}