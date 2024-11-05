package com.lambdaweather.data.model

import com.google.gson.annotations.SerializedName

class IpModel {
    @SerializedName("country2")
    val country2: String? = null

    @SerializedName("country3")
    val country3: String? = null

    @SerializedName("city")
    val city: String? = null

    @SerializedName("time_zone")
    val timeZone: String? = null

    @SerializedName("latitude")
    val latitude: Double? = null

    @SerializedName("longitude")
    val longitude: Double? = null

    @SerializedName("zip_code")
    val zipCode: String? = null

    @SerializedName("lang_code")
    val langCode: List<String>? = null

    @SerializedName("is_proxy")
    val isProxy: Boolean? = null

    @SerializedName("ASN")
    val asn: Int? = null

    @SerializedName("country_long")
    val countryLong: String? = null

    @SerializedName("region")
    val region: String? = null

    @SerializedName("ISP")
    val isp: String? = null

    @SerializedName("usage_type")
    val usageType: String? = null
}