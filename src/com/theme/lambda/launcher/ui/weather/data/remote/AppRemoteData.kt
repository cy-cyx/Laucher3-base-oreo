package com.lambdaweather.data.remote

import com.lambdaweather.data.AppException
import com.lambdaweather.data.NetworkHandler
import com.lambdaweather.data.Resource
import com.lambdaweather.data.model.AirModel
import com.lambdaweather.data.model.CityModel
import com.lambdaweather.data.model.ForestDayWeatherModel
import com.lambdaweather.data.model.ForestWeatherModel
import com.lambdaweather.data.model.IpModel
import com.lambdaweather.data.model.NewsModel
import com.lambdaweather.data.model.OneCallModel
import com.lambdaweather.data.model.WeatherModel
import com.lambdaweather.data.remote.api.AppService
import com.lambdaweather.utils.LocalUtils
import com.lambdaweather.utils.TimeUtils
import okhttp3.ResponseBody

class AppRemoteData(
    private val networkHandler: NetworkHandler,
    private val appService: AppService
) : AppDataSource {
    override suspend fun getWeather(
        lat: String?,
        lon: String?
    ): Resource<WeatherModel> {
        if (!networkHandler.isNetworkAvailable()) {
            return Resource.Failure(AppException.HttpException("Network Unavailable"))
        }
        return try {
            Resource.Success(
                appService.weather(
                    lat,
                    lon,
                    TimeUtils.getDateToString(TimeUtils.getCurrentTimeMillis(), "yyyymmddhh"),
                    LocalUtils.getCurrentLanguage()
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(AppException.LocalException(e.toString()))
        }
    }

    override suspend fun getGeo(
        lat: String?,
        lon: String?,
        q: String?,
        limit: String,
        appid: String?
    ): Resource<List<CityModel>> {
        if (!networkHandler.isNetworkAvailable()) {
            return Resource.Failure(AppException.HttpException("Network Unavailable"))
        }
        return try {
            Resource.Success(
                appService.geo(
                    lat,
                    lon,
                    q,
                    limit,
                    appid,
                    TimeUtils.getDateToString(TimeUtils.getCurrentTimeMillis(), "yyyymmddhh"),
                    LocalUtils.getCurrentLanguage()
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(AppException.LocalException(e.toString()))
        }
    }

    override suspend fun getGeoByLat(
        lat: String?,
        lon: String?,
        limit: String,
        appid: String?
    ): Resource<List<CityModel>> {
        if (!networkHandler.isNetworkAvailable()) {
            return Resource.Failure(AppException.HttpException("Network Unavailable"))
        }
        return try {
            Resource.Success(
                appService.reverse(
                    lat,
                    lon,
                    limit,
                    appid,
                    TimeUtils.getDateToString(TimeUtils.getCurrentTimeMillis(), "yyyymmddhh"),
                    LocalUtils.getCurrentLanguage()
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(AppException.LocalException(e.toString()))
        }
    }

    override suspend fun getForecastWeather(
        lat: String?,
        lon: String?
    ): Resource<ForestWeatherModel> {
        if (!networkHandler.isNetworkAvailable()) {
            return Resource.Failure(AppException.HttpException("Network Unavailable"))
        }
        return try {
            Resource.Success(
                appService.forecastWeather(
                    lat,
                    lon,
                    TimeUtils.getDateToString(TimeUtils.getCurrentTimeMillis(), "yyyymmddhh"),
                    LocalUtils.getCurrentLanguage()
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(AppException.LocalException(e.toString()))
        }
    }

    override suspend fun getForecastAir(
        lat: String?,
        lon: String?,
        appid: String?
    ): Resource<AirModel> {
        if (!networkHandler.isNetworkAvailable()) {
            return Resource.Failure(AppException.HttpException("Network Unavailable"))
        }
        return try {
            Resource.Success(
                appService.forecastAir(
                    lat,
                    lon,
                    appid,
                    TimeUtils.getDateToString(TimeUtils.getCurrentTimeMillis(), "yyyymmddhh"),
                    LocalUtils.getCurrentLanguage()
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(AppException.LocalException(e.toString()))
        }
    }

    override suspend fun getIp(): Resource<IpModel> {
        if (!networkHandler.isNetworkAvailable()) {
            return Resource.Failure(AppException.HttpException("Network Unavailable"))
        }
        return try {
            Resource.Success(appService.ip())
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(AppException.LocalException(e.toString()))
        }
    }

    override suspend fun getNews(map: Map<String, String>): Resource<NewsModel> {
        if (!networkHandler.isNetworkAvailable()) {
            return Resource.Failure(AppException.HttpException("Network Unavailable"))
        }
        return try {
            Resource.Success(appService.news(map))
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(AppException.LocalException(e.toString()))
        }
    }

    override suspend fun getEarthquake(): Resource<ResponseBody> {
        if (!networkHandler.isNetworkAvailable()) {
            return Resource.Failure(AppException.HttpException("Network Unavailable"))
        }
        return try {
            Resource.Success(appService.earthquake())
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(AppException.LocalException(e.toString()))
        }
    }

    override suspend fun getAlert(map: Map<String, String>): Resource<OneCallModel> {
        if (!networkHandler.isNetworkAvailable()) {
            return Resource.Failure(AppException.HttpException("Network Unavailable"))
        }
        return try {
            Resource.Success(appService.alert(map))
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(AppException.LocalException(e.toString()))
        }
    }

    override suspend fun getForecastHourWeather(
        map: Map<String, String>
    ): Resource<ForestWeatherModel> {
        if (!networkHandler.isNetworkAvailable()) {
            return Resource.Failure(AppException.HttpException("Network Unavailable"))
        }
        return try {
            Resource.Success(
                appService.forecastHourWeather(
                    map
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(AppException.LocalException(e.toString()))
        }
    }

    override suspend fun getForecastDay7Weather(map: Map<String, String>): Resource<ForestDayWeatherModel> {
        if (!networkHandler.isNetworkAvailable()) {
            return Resource.Failure(AppException.HttpException("Network Unavailable"))
        }
        return try {
            Resource.Success(
                appService.forecastDay7Weather(
                    map
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(AppException.LocalException(e.toString()))
        }
    }
}
