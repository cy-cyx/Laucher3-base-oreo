package com.theme.lambda.launcher.data.api

import com.lambda.common.data.model.BaseResult
import com.theme.lambda.launcher.data.model.ForestDayWeather
import com.theme.lambda.launcher.data.model.ForestWeather
import com.lambda.news.data.model.NewResult
import com.theme.lambda.launcher.data.model.ResResult
import com.theme.lambda.launcher.data.model.Weather
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface AppApi {

    @GET("news/v1/search_news")
    suspend fun getNewData(
        @Query("countries") countries: String?,
        @Query("languages") languages: String?,
        @Query("page") page: String?,
        @Query("page_size") pageSize: String?,
        @Query("categories") categories: String?,
        @Query("publish_date_from") publishDateFrom: String?,
        @Query("sort") sort: String?,
        @Query("fallback") fallback: String?,
    ): BaseResult<NewResult>

    @GET("api/v2/query_tag_resources")
    suspend fun getResource(
        @QueryMap map: Map<String, String>
    ): BaseResult<ResResult>

    @GET("/api/v1/open_weather_map?path=/data/2.5/weather&")
    suspend fun weather(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("lang") lang: String?,
    ): Weather

    @GET("/api/v1/open_weather_map?path=/data/2.5/forecast&")
    suspend fun forecastWeather(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("lang") lang: String?,
    ): ForestWeather

    @GET("/api/v1/open_weather_map?path=/data/2.5/forecast/daily")
    suspend fun forecastDay7Weather(
        @QueryMap map: Map<String, String>
    ): ForestDayWeather
}