package com.theme.lambda.launcher.data.model

import com.google.gson.annotations.SerializedName

class ResResult {

    @SerializedName("resources")
    var resources = ArrayList<Resources>()
}

class Resources {

    @SerializedName("access_type")
    var accessType: Int = 0

    @SerializedName("id")
    var id: String = ""

    @SerializedName("preview_url")
    var previewUrl: String = ""

    @SerializedName("zip_url")
    var zipUrl: String = ""

    var tag = ""

    fun toThemeRes(): ThemeRes {
        val themeRes = ThemeRes()
        themeRes.let {
            it.did = this.id
            it.previewUrl = this.previewUrl
            it.zipUrl = this.zipUrl
            it.accessType = this.accessType
            it.downloadDate = System.currentTimeMillis()
        }
        return themeRes
    }
}