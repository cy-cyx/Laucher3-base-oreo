package com.lambda.common.data.model

import com.google.gson.annotations.SerializedName

class BaseResult<T> {

    @SerializedName("c")
    val code: Int = 0

    @SerializedName("d")
    val d: T? = null
}