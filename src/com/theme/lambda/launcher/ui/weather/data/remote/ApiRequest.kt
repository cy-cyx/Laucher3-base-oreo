package com.lambdaweather.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiRequest(
    @SerialName("binary_data_base64")
    val binaryDataBase64: List<String> = listOf(),
    @SerialName("target_age")
    val targetAge: Int = 0,
    @SerialName("req_key")
    val reqKey: String = "",
    @SerialName("req_data")
    val reqData: ReqData = ReqData(),
    @SerialName("action_id")
    val actionId: String = "",
    @SerialName("template_base64")
    val templateBase64: String = "",
    @SerialName("image_base64")
    val imageBase64: String = "",
    @SerialName("version")
    val version: String = "",
    @SerialName("boy_base64")
    val boyBase64: String = "",
    @SerialName("girl_base64")
    val girlBase64: String = "",
)

@Serializable
data class ReqData(
    @SerialName("effect_image_only")
    val effectImageOnly: Boolean = false,
)

