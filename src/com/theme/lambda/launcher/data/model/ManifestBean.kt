package com.theme.lambda.launcher.data.model

import com.google.gson.annotations.SerializedName

class ManifestBean {

    @SerializedName("icons")
    var icons: ArrayList<IconBean> = arrayListOf()

    @SerializedName("background")
    var background: String = ""
}

class IconBean {
    @SerializedName("pn")
    var pn: String = ""

    @SerializedName("icon")
    var icon: String = ""
}