package com.lambdaweather.factory

import androidx.lifecycle.LifecycleCoroutineScope
import com.lambdaweather.LambdaWeather
import com.lambdaweather.data.NetworkHandler
import com.lambdaweather.data.model.WeatherModel
import com.lambdaweather.data.remote.AppRemoteData
import com.lambdaweather.data.remote.api.AppService
import com.lambdaweather.data.repository.AppRepository
import com.lambdaweather.data.repository.AppRepositorySource
import com.lambdaweather.utils.WeatherUtils
import com.lambda.common.net.RetrofitUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object RetrofitFactory {
    lateinit var appService: AppService
    lateinit var appRepositorySource: AppRepositorySource
    fun init() {
        appService = AppService(RetrofitUtil.retrofit)
        appRepositorySource = AppRepository(
            AppRemoteData(NetworkHandler(LambdaWeather.application), appService),
            Dispatchers.IO
        )
    }

    var weatherModelTemp: WeatherModel? = null
    fun requestLocationData(
        lifecycleScope: LifecycleCoroutineScope,
        callback: (WeatherModel?) -> Unit
    ) {
        if (null != weatherModelTemp) {
            callback.invoke(weatherModelTemp)
            return
        }
        val location = WeatherUtils.getSelectLocation()
        if (location == null) {
            lifecycleScope.launch {
                appRepositorySource.getIp().collect {
                    appRepositorySource.getWeather(
                        it.data?.latitude.toString(),
                        it.data?.longitude.toString()
                    ).collect {
                        weatherModelTemp = it.data
                        callback.invoke(weatherModelTemp)
                    }
                }
            }
        } else {
            lifecycleScope.launch {
                appRepositorySource.getWeather(location.lat.toString(), location.lon.toString())
                    .collect {
                        weatherModelTemp = it.data
                        callback.invoke(weatherModelTemp)
                    }
            }
        }
    }

}