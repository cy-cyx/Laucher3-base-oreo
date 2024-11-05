package com.lambdaweather.data.model

import com.google.gson.annotations.SerializedName

class OneCallModel {

    @SerializedName("lat")
    val lat: Double? = null

    @SerializedName("lon")
    val lon: Double? = null

    @SerializedName("timezone")
    val timezone: String? = null

    @SerializedName("timezone_offset")
    val timezoneOffset: Int? = null

    @SerializedName("current")
    val current: CurrentDTO? = null

    @SerializedName("minutely")
    val minutely: List<MinutelyDTO>? = null

    @SerializedName("hourly")
    val hourly: List<HourlyDTO>? = null

    @SerializedName("daily")
    val daily: List<DailyDTO>? = null

    @SerializedName("alerts")
    val alerts: List<AlertsDTO>? = null

    class CurrentDTO {
        @SerializedName("dt")
        val dt: Int? = null

        @SerializedName("sunrise")
        val sunrise: Int? = null

        @SerializedName("sunset")
        val sunset: Int? = null

        @SerializedName("temp")
        val temp: Double? = null

        @SerializedName("feels_like")
        val feelsLike: Double? = null

        @SerializedName("pressure")
        val pressure: Int? = null

        @SerializedName("humidity")
        val humidity: Int? = null

        @SerializedName("dew_point")
        val dewPoint: Double? = null

        @SerializedName("uvi")
        val uvi: Double? = null

        @SerializedName("clouds")
        val clouds: Int? = null

        @SerializedName("visibility")
        val visibility: Int? = null

        @SerializedName("wind_speed")
        val windSpeed: Double? = null

        @SerializedName("wind_deg")
        val windDeg: Int? = null

        @SerializedName("wind_gust")
        val windGust: Double? = null

        @SerializedName("weather")
        val weather: List<WeatherDTO>? = null

        @SerializedName("rain")
        val rain: RainDTO? = null

        class RainDTO {
            @SerializedName("1h")
            val `$1h`: Double? = null
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

    class MinutelyDTO {
        @SerializedName("dt")
        val dt: Int? = null

        @SerializedName("precipitation")
        val precipitation: Double? = null
    }

    class HourlyDTO {
        @SerializedName("dt")
        val dt: Int? = null

        @SerializedName("temp")
        val temp: Double? = null

        @SerializedName("feels_like")
        val feelsLike: Double? = null

        @SerializedName("pressure")
        val pressure: Int? = null

        @SerializedName("humidity")
        val humidity: Int? = null

        @SerializedName("dew_point")
        val dewPoint: Double? = null

        @SerializedName("uvi")
        val uvi: Double? = null

        @SerializedName("clouds")
        val clouds: Int? = null

        @SerializedName("visibility")
        val visibility: Int? = null

        @SerializedName("wind_speed")
        val windSpeed: Double? = null

        @SerializedName("wind_deg")
        val windDeg: Int? = null

        @SerializedName("wind_gust")
        val windGust: Double? = null

        @SerializedName("weather")
        val weather: List<WeatherDTO>? = null

        @SerializedName("pop")
        val pop: Double? = null

        @SerializedName("rain")
        val rain: RainDTO? = null

        class RainDTO {
            @SerializedName("1h")
            val `$1h`: Double? = null
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

    class DailyDTO {
        @SerializedName("dt")
        val dt: Int? = null

        @SerializedName("sunrise")
        val sunrise: Int? = null

        @SerializedName("sunset")
        val sunset: Int? = null

        @SerializedName("moonrise")
        val moonrise: Int? = null

        @SerializedName("moonset")
        val moonset: Int? = null

        @SerializedName("moon_phase")
        val moonPhase: Double? = null

        @SerializedName("temp")
        val temp: TempDTO? = null

        @SerializedName("feels_like")
        val feelsLike: FeelsLikeDTO? = null

        @SerializedName("pressure")
        val pressure: Int? = null

        @SerializedName("humidity")
        val humidity: Int? = null

        @SerializedName("dew_point")
        val dewPoint: Double? = null

        @SerializedName("wind_speed")
        val windSpeed: Double? = null

        @SerializedName("wind_deg")
        val windDeg: Int? = null

        @SerializedName("wind_gust")
        val windGust: Double? = null

        @SerializedName("weather")
        val weather: List<WeatherDTO>? = null

        @SerializedName("clouds")
        val clouds: Int? = null

        @SerializedName("pop")
        val pop: Double? = null

        @SerializedName("rain")
        val rain: Double? = null

        @SerializedName("uvi")
        val uvi: Double? = null

        class TempDTO {
            @SerializedName("day")
            val day: Double? = null

            @SerializedName("min")
            val min: Double? = null

            @SerializedName("max")
            val max: Double? = null

            @SerializedName("night")
            val night: Double? = null

            @SerializedName("eve")
            val eve: Double? = null

            @SerializedName("morn")
            val morn: Double? = null
        }

        class FeelsLikeDTO {
            @SerializedName("day")
            val day: Double? = null

            @SerializedName("night")
            val night: Double? = null

            @SerializedName("eve")
            val eve: Double? = null

            @SerializedName("morn")
            val morn: Double? = null
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

    class AlertsDTO {
        @SerializedName("sender_name")
        val senderName: String? = null

        @SerializedName("event")
        val event: String? = null

        @SerializedName("start")
        val start: Long? = null

        @SerializedName("end")
        val end: Long? = null

        @SerializedName("description")
        val description: String? = null

        @SerializedName("tags")
        val tags: List<String>? = null
    }
}