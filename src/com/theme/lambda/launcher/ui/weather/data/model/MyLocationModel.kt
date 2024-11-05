package com.lambdaweather.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class MyLocationModel(
    val lat: String,
    val lon: String,
    val locality: String?,
    val thoroughfare: String?,
    val isLocation: Boolean = false,
    var isSelect: Boolean = false,
    var temp: String? = null,
    var tempLowUp: String? = null,
    var weather: String? = null,
    var country: String? = null
) : Parcelable {

    fun getLocalName(): String {
        return "$locality"
    }
}