package com.lambdaweather.data.repository

import com.lambdaweather.data.Resource
import com.lambdaweather.data.model.AirModel
import com.lambdaweather.data.model.CityModel
import com.lambdaweather.data.model.ForestDayWeatherModel
import com.lambdaweather.data.model.ForestWeatherModel
import com.lambdaweather.data.model.IpModel
import com.lambdaweather.data.model.NewsModel
import com.lambdaweather.data.model.OneCallModel
import com.lambdaweather.data.model.WeatherModel
import kotlinx.coroutines.flow.Flow

interface AppRepositorySource {
    suspend fun getWeather(
        lat: String, lon: String
    ): Flow<Resource<WeatherModel>>

    suspend fun getGeo(
        lat: String?, lon: String?, q: String?
    ): Flow<Resource<List<CityModel>>>

    suspend fun getGeoByLat(
        lat: String?, lon: String?
    ): Flow<Resource<List<CityModel>>>

    suspend fun getForecastWeather(
        lat: String, lon: String
    ): Flow<Resource<ForestWeatherModel>>

    suspend fun getForecastAirPollution(
        lat: String, lon: String
    ): Flow<Resource<AirModel>>

    suspend fun getIp(
    ): Flow<Resource<IpModel>>

    suspend fun getNews(
        country: String, page: String
    ): Flow<Resource<NewsModel>>

    suspend fun getAlert(
        lat: String, lon: String
    ): Flow<Resource<OneCallModel>>

    suspend fun getForecastHourWeather(
        lat: String, lon: String
    ): Flow<Resource<ForestWeatherModel>>

    suspend fun getForecastDay7Weather(
        lat: String, lon: String
    ): Flow<Resource<ForestDayWeatherModel>>

}