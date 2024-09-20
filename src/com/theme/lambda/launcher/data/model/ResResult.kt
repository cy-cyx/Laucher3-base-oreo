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
}