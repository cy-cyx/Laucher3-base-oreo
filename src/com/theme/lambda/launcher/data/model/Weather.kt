package com.theme.lambda.launcher.data.model

import com.google.gson.annotations.SerializedName

class Weather {
    @SerializedName("coord")
    val coord: CoordDTO? = null

    @SerializedName("weather")
    val weather: List<WeatherDTO>? = null

    @SerializedName("base")
    val base: String? = null

    @SerializedName("main")
    val main: MainDTO? = null

    @SerializedName("visibility")
    val visibility: Int? = null

    @SerializedName("wind")
    val wind: WindDTO? = null

    @SerializedName("clouds")
    val clouds: CloudsDTO? = null

    @SerializedName("rain")
    val rain: RainDTO? = null

    @SerializedName("dt")
    val dt: Int? = null

    @SerializedName("sys")
    val sys: SysDTO? = null

    @SerializedName("timezone")
    val timezone: Int? = null

    @SerializedName("id")
    val id: Int? = null

    @SerializedName("name")
    val name: String? = null

    @SerializedName("cod")
    val cod: Int? = null

    class CoordDTO {
        @SerializedName("lon")
        val lon: Double? = null

        @SerializedName("lat")
        val lat: Double? = null
    }

    class MainDTO {
        @SerializedName("temp")
        val temp: Double? = null

        @SerializedName("feels_like")
        val feelsLike: Double? = null

        @SerializedName("temp_min")
        val tempMin: Double? = null

        @SerializedName("temp_max")
        val tempMax: Double? = null

        @SerializedName("pressure")
        val pressure: Int? = null

        @SerializedName("humidity")
        val humidity: Int? = null

        @SerializedName("sea_level")
        val seaLevel: Int? = null

        @SerializedName("grnd_level")
        val grndLevel: Int? = null
    }

    class WindDTO {
        @SerializedName("speed")
        val speed: Double? = null

        @SerializedName("deg")
        val deg: Int? = null

        @SerializedName("gust")
        val gust: Double? = null
    }

    class CloudsDTO {
        @SerializedName("all")
        val all: Int? = null
    }

    class RainDTO {
        @SerializedName("1h")
        val h1: Double? = null

        @SerializedName("3h")
        val h3: Double? = null
    }

    class SysDTO {
        @SerializedName("type")
        val type: Int? = null

        @SerializedName("id")
        val id: Int? = null

        @SerializedName("country")
        val country: String? = null

        @SerializedName("sunrise")
        val sunrise: Int? = null

        @SerializedName("sunset")
        val sunset: Int? = null
    }

    class WeatherDTO {
        @SerializedName("id")
        val id: Int? = null

        @SerializedName("main")
        val main: String? = null

        @SerializedName("description")
        val description: String? = null

        @SerializedName("icon")
        val icon: String? = null
    }
}