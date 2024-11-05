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
import com.lambdaweather.data.remote.AppRemoteData
import com.lambdaweather.utils.LocalUtils
import com.lambdaweather.utils.TimeUtils
import com.lambdaweather.utils.toCustomInt
import com.theme.lambda.launcher.utils.TimeUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

class AppRepository(
    private val appRemoteRemoteData: AppRemoteData,
    private val ioDispatcher: CoroutineContext
) : AppRepositorySource {

    override suspend fun getWeather(lat: String, lon: String): Flow<Resource<WeatherModel>> {
        return flow {
            emit(
                appRemoteRemoteData.getWeather(
                    lat.toCustomInt(), lon.toCustomInt(),
                )
            )
        }.flowOn(ioDispatcher)
    }

    override suspend fun getGeo(
        lat: String?,
        lon: String?,
        q: String?
    ): Flow<Resource<List<CityModel>>> {
        return flow {
            emit(
                appRemoteRemoteData.getGeo(
                    lat.toCustomInt(), lon.toCustomInt(),
                    q, "5", null
                )
            )
        }.flowOn(ioDispatcher)
    }

    override suspend fun getGeoByLat(lat: String?, lon: String?): Flow<Resource<List<CityModel>>> {
        return flow {
            emit(
                appRemoteRemoteData.getGeoByLat(
                    lat.toCustomInt(), lon.toCustomInt(),
                    "5", null
                )
            )
        }.flowOn(ioDispatcher)
    }

    override suspend fun getForecastWeather(
        lat: String,
        lon: String
    ): Flow<Resource<ForestWeatherModel>> {
        return flow {
            emit(
                appRemoteRemoteData.getForecastWeather(
                    lat.toCustomInt(), lon.toCustomInt()
                )
            )
        }.flowOn(ioDispatcher)
    }

    override suspend fun getForecastAirPollution(
        lat: String,
        lon: String
    ): Flow<Resource<AirModel>> {
        return flow {
            emit(
                appRemoteRemoteData.getForecastAir(
                    lat.toCustomInt(), lon.toCustomInt(), null
                )
            )
        }.flowOn(ioDispatcher)
    }

    override suspend fun getIp(): Flow<Resource<IpModel>> {
        return flow {
            emit(
                appRemoteRemoteData.getIp(
                )
            )
        }.flowOn(ioDispatcher)
    }

    override suspend fun getNews(
        country: String,
        page: String,
    ): Flow<Resource<NewsModel>> {
        return flow {
            emit(
                appRemoteRemoteData.getNews(
                    mapOf(
                        "countries" to LocalUtils.getCountry(),
                        "page" to (page).toInt().toString(),
                        "page_size" to "25",
                        "publish_date_from" to "${TimeUtil.getOldDate(-365)}",
                        "sort" to "publish_date,desc",
                        "language" to LocalUtils.getCurrentLanguage(),
                        "fallback" to "true"
                    )
                )
            )
        }.flowOn(ioDispatcher)
    }

    override suspend fun getAlert(lat: String, lon: String): Flow<Resource<OneCallModel>> {
        return flow {
            emit(
                appRemoteRemoteData.getAlert(
                    mapOf(
                        "lat" to lat.toCustomInt(),
                        "lon" to lon.toCustomInt(),
                        "lang" to LocalUtils.getCurrentLanguage(),
                        "hour" to TimeUtils.getDateToString(
                            TimeUtils.getCurrentTimeMillis(),
                            "yyyymmddhh"
                        ),
                        "exclude" to "minutely"
                    )
                )
            )
        }.flowOn(ioDispatcher)
    }

    override suspend fun getForecastHourWeather(
        lat: String,
        lon: String
    ): Flow<Resource<ForestWeatherModel>> {
        return flow {
            emit(
                appRemoteRemoteData.getForecastHourWeather(
                    mapOf(
                        "lat" to lat.toCustomInt(),
                        "lon" to lon.toCustomInt(),
                        "lang" to LocalUtils.getCurrentLanguage(),
                        "hour" to TimeUtils.getDateToString(
                            TimeUtils.getCurrentTimeMillis(),
                            "yyyymmddhh"
                        ),
                        "cnt" to "24",
                        "host" to "pro.openweathermap.org"
                    )
                )
            )
        }.flowOn(ioDispatcher)
    }

    override suspend fun getForecastDay7Weather(
        lat: String,
        lon: String
    ): Flow<Resource<ForestDayWeatherModel>> {
        return flow {
            emit(
                appRemoteRemoteData.getForecastDay7Weather(
                    mapOf(
                        "lat" to lat.toCustomInt(),
                        "lon" to lon.toCustomInt(),
                        "lang" to LocalUtils.getCurrentLanguage(),
                        "hour" to TimeUtils.getDateToString(
                            TimeUtils.getCurrentTimeMillis(),
                            "yyyymmddhh"
                        ),
                        "cnt" to "7",
                        "host" to "api.openweathermap.org"
                    )
                )
            )
        }.flowOn(ioDispatcher)
    }


}