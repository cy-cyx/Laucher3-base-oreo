package com.theme.lambda.launcher.data.model

import com.google.gson.annotations.SerializedName

data class Package(
    @SerializedName("name")
    val name: String?,
    @SerializedName("package")
    val packageX: String?
)