package com.lambdaweather.factory

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import com.lambdaweather.LambdaWeather
import com.lambdaweather.data.NetworkHandler
import com.lambdaweather.data.model.WeatherModel
import com.lambdaweather.data.remote.AppRemoteData
import com.lambdaweather.data.remote.api.AppService
import com.lambdaweather.data.repository.AppRepository
import com.lambdaweather.data.repository.AppRepositorySource
import com.lambdaweather.utils.GsonUtil
import com.lambdaweather.utils.WeatherUtils
import com.theme.lambda.launcher.ui.weather.data.LenientGsonConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object RetrofitFactory {
    lateinit var retrofit : Retrofit
    lateinit var appService : AppService
    lateinit var appRepositorySource : AppRepositorySource
    fun init() {
        retrofit = Retrofit.Builder()
            .client(OkHttpClient.Builder()
                .hostnameVerifier { _, _ -> true }
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor { message ->
                    Log.d(
                        "RetrofitFactory",
                        "message：$message"
                    )
                }.setLevel(HttpLoggingInterceptor.Level.BODY)).build()
            ).baseUrl(LambdaWeather.baseUrl)
            .addConverterFactory(LenientGsonConverterFactory.create(GsonUtil.mGson))
            .build()

        appService = AppService(retrofit)
        appRepositorySource = AppRepository(
            AppRemoteData(NetworkHandler(LambdaWeather.application),appService),
            Dispatchers.IO
        )
    }

    var weatherModelTemp : WeatherModel ?= null
    fun requestLocationData(lifecycleScope : LifecycleCoroutineScope,callback : (WeatherModel?)->Unit){
        if(null != weatherModelTemp){
            callback.invoke(weatherModelTemp)
            return
        }
        val location = WeatherUtils.getSelectLocation()
        if(location == null){
            lifecycleScope.launch {
                appRepositorySource.getIp().collect {
                    appRepositorySource.getWeather(it.data?.latitude.toString(),it.data?.longitude.toString()).collect {
                        weatherModelTemp = it.data
                        callback.invoke(weatherModelTemp)
                    }
                }
            }
        }else{
            lifecycleScope.launch {
                appRepositorySource.getWeather(location.lat.toString(),location.lon.toString()).collect {
                    weatherModelTemp = it.data
                    callback.invoke(weatherModelTemp)
                }
            }
        }
    }

}