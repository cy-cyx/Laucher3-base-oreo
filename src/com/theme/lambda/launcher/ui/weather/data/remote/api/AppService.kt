package com.lambdaweather.data.remote.api

import com.lambdaweather.data.model.AirModel
import com.lambdaweather.data.model.CityModel
import com.lambdaweather.data.model.ForestDayWeatherModel
import com.lambdaweather.data.model.ForestWeatherModel
import com.lambdaweather.data.model.IpModel
import com.lambdaweather.data.model.NewsModel
import com.lambdaweather.data.model.OneCallModel
import com.lambdaweather.data.model.WeatherModel
import com.lambdaweather.utils.TimeUtils
import okhttp3.ResponseBody
import retrofit2.Retrofit

class AppService(retrofit: Retrofit) : AppApi {
    private val appApi: AppApi by lazy { retrofit.create(AppApi::class.java) }

    override suspend fun weather(
        lat: String?,
        lon: String?,
        hour: String?,
        lang: String?,
    ): WeatherModel {
        return appApi.weather(
            lat,
            lon,
            hour, lang
        )
    }

    override suspend fun geo(
        lat: String?,
        lon: String?,
        q: String?,
        limit: String,
        appid: String?,
        hour: String?,
        lang: String?,
    ): List<CityModel> {
        return appApi.geo(
            lat,
            lon,
            q,
            limit,
            appid,
            hour, lang
        )
    }

    override suspend fun reverse(
        lat: String?,
        lon: String?,
        limit: String,
        appid: String?,
        hour: String?,
        lang: String?,
    ): List<CityModel> {
        return appApi.reverse(
            lat,
            lon,
            limit,
            appid,
            hour, lang
        )
    }

    override suspend fun forecastWeather(
        lat: String?,
        lon: String?,
        hour: String?,
        lang: String?,
    ): ForestWeatherModel {
        return appApi.forecastWeather(
            lat,
            lon,
            hour, lang
        )
    }

    override suspend fun forecastAir(
        lat: String?,
        lon: String?,
        appid: String?,
        hour: String?,
        lang: String?,
    ): AirModel {
        return appApi.forecastAir(
            lat,
            lon,
            appid,
            hour, lang
        )
    }

    override suspend fun ip(): IpModel {
        return appApi.ip()
    }

    override suspend fun news(map: Map<String, String>): NewsModel {
        return appApi.news(map.apply {
            this.toMutableMap()
        })
    }

    override suspend fun earthquake(): ResponseBody {
        return appApi.earthquake()
    }

    override suspend fun alert(map: Map<String, String>): OneCallModel {
        return appApi.alert(map)
    }

    override suspend fun forecastHourWeather(map: Map<String, String>): ForestWeatherModel {
        return appApi.forecastHourWeather(map)
    }

    override suspend fun forecastDay7Weather(map: Map<String, String>): ForestDayWeatherModel {
        return appApi.forecastDay7Weather(map)
    }
}