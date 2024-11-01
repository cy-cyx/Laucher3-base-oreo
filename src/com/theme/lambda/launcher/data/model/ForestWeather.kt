package com.theme.lambda.launcher.data.model

import com.google.gson.annotations.SerializedName

class ForestWeather {
    @SerializedName("cod")
    val cod: String? = null

    @SerializedName("message")
    val message: Int? = null

    @SerializedName("cnt")
    val cnt: Int? = null

    @SerializedName("list")
    val list: List<ListDTO>? = null

    @SerializedName("city")
    val city: CityDTO? = null

    class CityDTO {
        @SerializedName("id")
        val id: Int? = null

        @SerializedName("name")
        val name: String? = null

        @SerializedName("coord")
        val coord: CoordDTO? = null

        @SerializedName("country")
        val country: String? = null

        @SerializedName("population")
        val population: Int? = null

        @SerializedName("timezone")
        val timezone: Int? = null

        @SerializedName("sunrise")
        val sunrise: Int? = null

        @SerializedName("sunset")
        val sunset: Int? = null

        class CoordDTO {
            @SerializedName("lat")
            val lat: Float? = null

            @SerializedName("lon")
            val lon: Float? = null
        }
    }

    class ListDTO {
        @SerializedName("dt")
        var dt: Long? = null

        @SerializedName("main")
        val main: MainDTO? = null

        @SerializedName("weather")
        val weather: List<WeatherDTO>? = null

        @SerializedName("clouds")
        val clouds: CloudsDTO? = null

        @SerializedName("rain")
        val rain: RainDTO? = null

        @SerializedName("wind")
        val wind: WindDTO? = null

        @SerializedName("visibility")
        val visibility: Int? = null

        @SerializedName("pop")
        val pop: Float? = null

        @SerializedName("sys")
        val sys: SysDTO? = null

        @SerializedName("dt_txt")
        val dtTxt: String? = null

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

            @SerializedName("sea_level")
            val seaLevel: Int? = null

            @SerializedName("grnd_level")
            val grndLevel: Int? = null

            @SerializedName("humidity")
            val humidity: Int? = null

            @SerializedName("temp_kf")
            val tempKf: Double? = null

            var tempDailyMax: Double? = null

            var tempDailyMin: Double? = null
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

        class WindDTO {
            @SerializedName("speed")
            val speed: Double? = null

            @SerializedName("deg")
            val deg: Int? = null

            @SerializedName("gust")
            val gust: Double? = null
        }

        class SysDTO {
            @SerializedName("pod")
            val pod: String? = null
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

    fun getDailyMaxTemp(date: String): Double {
        var tempMax: Double? = null
        this.list?.forEach {
            if (!it.dtTxt!!.contains(date)) {
                return@forEach
            }
            tempMax = if (null == tempMax) {
                it.main?.tempMax
            } else {
                it.main?.tempMax?.let { it1 -> (tempMax!!).coerceAtLeast(it1) }
            }
        }
        if (tempMax == null) {
            tempMax = Double.MIN_VALUE
            this.list?.forEach {
                it.main?.tempMax?.let {
                    if (it > tempMax!!) {
                        tempMax = it
                    }
                }
            }
        }
        return tempMax ?: 0.0
    }


    fun getDailyMinTemp(date: String): Double {
        var tempMin: Double? = null
        this.list?.forEach {
            if (!it.dtTxt!!.contains(date)) {
                return@forEach
            }
            tempMin = if (null == tempMin) {
                it.main?.tempMin
            } else {
                it.main?.tempMin?.let { it1 -> (tempMin!!).coerceAtMost(it1) }
            }
        }
        if (tempMin == null) {
            tempMin = Double.MAX_VALUE
            this.list?.forEach {
                it.main?.tempMin?.let {
                    if (it < tempMin!!) {
                        tempMin = it
                    }
                }
            }
        }
        return tempMin ?: 0.0
    }
}