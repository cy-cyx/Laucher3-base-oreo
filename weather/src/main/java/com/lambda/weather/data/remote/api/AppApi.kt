package com.lambdaweather.data.remote.api

import com.lambdaweather.data.model.*
import okhttp3.ResponseBody
import retrofit2.http.*

interface AppApi {

    @GET("/api/v1/open_weather_map?path=/data/2.5/weather&")
    suspend fun weather(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("hour") hour: String?,
        @Query("lang") lang: String?,
    ): WeatherModel

    @GET("/api/v1/open_weather_map?path=/geo/1.0/direct&")
    suspend fun geo(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("q") q: String?,
        @Query("limit") limit: String,
        @Query("appid") appid: String?,
        @Query("hour") hour: String?,
        @Query("lang") lang: String?,
    ): List<CityModel>

    @GET("/api/v1/open_weather_map?path=/geo/1.0/reverse&")
    suspend fun reverse(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("limit") limit: String,
        @Query("appid") appid: String?,
        @Query("hour") hour: String?,
        @Query("lang") lang: String?,
    ): List<CityModel>

    @GET("/api/v1/open_weather_map?path=/data/2.5/forecast&")
    suspend fun forecastWeather(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("hour") hour: String?,
        @Query("lang") lang: String?,
    ): ForestWeatherModel

    @GET("/api/v1/open_weather_map?path=/data/2.5/air_pollution/forecast&")
    suspend fun forecastAir(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("appid") appid: String?,
        @Query("hour") hour: String?,
        @Query("lang") lang: String?,
    ): AirModel

    @GET("/api/v1/ip")
    suspend fun ip(
    ): IpModel

    @GET("/news/v1/search_news")
    suspend fun news(
        @QueryMap map: Map<String, String>
    ): NewsModel

    @GET("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.geojsonp")
    suspend fun earthquake(
    ): ResponseBody

    @GET("/api/v1/open_weather_map?path=/data/3.0/onecall")
    suspend fun alert(
        @QueryMap map: Map<String, String>
    ): OneCallModel

    @GET("/api/v1/open_weather_map?path=/data/2.5/forecast/hourly")
    suspend fun forecastHourWeather(
        @QueryMap map: Map<String, String>
    ): ForestWeatherModel

    @GET("/api/v1/open_weather_map?path=/data/2.5/forecast/daily")
    suspend fun forecastDay7Weather(
        @QueryMap map: Map<String, String>
    ): ForestDayWeatherModel
}