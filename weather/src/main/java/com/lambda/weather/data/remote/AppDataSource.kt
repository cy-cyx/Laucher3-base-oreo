package com.lambdaweather.data.remote

import com.lambdaweather.data.Resource
import com.lambdaweather.data.model.AirModel
import com.lambdaweather.data.model.CityModel
import com.lambdaweather.data.model.ForestDayWeatherModel
import com.lambdaweather.data.model.ForestWeatherModel
import com.lambdaweather.data.model.IpModel
import com.lambdaweather.data.model.NewsModel
import com.lambdaweather.data.model.OneCallModel
import com.lambdaweather.data.model.WeatherModel
import okhttp3.ResponseBody


internal interface AppDataSource {

    suspend fun getWeather(
        lat: String?,
        lon: String?
    ): Resource<WeatherModel>

    suspend fun getGeo(
        lat: String?, lon: String?,
        q: String?, limit: String, appid: String?
    ): Resource<List<CityModel>>

    suspend fun getGeoByLat(
        lat: String?, lon: String?, limit: String, appid: String?
    ): Resource<List<CityModel>>

    suspend fun getForecastWeather(
        lat: String?,
        lon: String?
    ): Resource<ForestWeatherModel>

    suspend fun getForecastAir(
        lat: String?,
        lon: String?,
        appid: String?
    ): Resource<AirModel>

    suspend fun getIp(
    ): Resource<IpModel>

    suspend fun getNews(
        map: Map<String, String>,
    ): Resource<NewsModel>

    suspend fun getEarthquake(
    ): Resource<ResponseBody>

    suspend fun getAlert(
        map: Map<String, String>,
    ): Resource<OneCallModel>

    suspend fun getForecastHourWeather(
        map: Map<String, String>
    ): Resource<ForestWeatherModel>

    suspend fun getForecastDay7Weather(
        map: Map<String, String>
    ): Resource<ForestDayWeatherModel>
}