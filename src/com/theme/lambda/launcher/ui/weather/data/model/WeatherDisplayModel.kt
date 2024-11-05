package com.lambdaweather.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class WeatherDisplayModel(
    val title: String,
    val time: Long, val icon: String, val weather: String, val maxT: String, val minT: String,
    val location: String, val windSpeed: String, val rain: String, val air: String, val hum: String
) : Parcelable