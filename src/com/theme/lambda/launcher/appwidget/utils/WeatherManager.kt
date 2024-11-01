package com.theme.lambda.launcher.appwidget.utils

import com.android.launcher3.R
import com.theme.lambda.launcher.data.DataRepository
import com.theme.lambda.launcher.data.model.ForestDayWeather
import com.theme.lambda.launcher.data.model.ForestWeather
import com.theme.lambda.launcher.data.model.Weather
import com.theme.lambda.launcher.utils.LocalUtil
import com.theme.lambda.launcher.utils.TimeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

object WeatherManager {

    private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val mainScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    var weather: Weather? = null
    var forestWeather: ForestWeather? = null
    var forestDayWeather: ForestDayWeather? = null

    private var lastUpDataTimeStamp = -1L

    enum class WeatherUnit(val id: Int, val unitName: String) {
        C(0, "℃"),
        F(1, "℉")
    }

    fun init() {
        LocalUtil.getLocal { _, _ ->
            initWeatherData()
        }
    }

    private fun initWeatherData() {
        ioScope.launch {
            weather = DataRepository.getWeather()
            forestWeather = DataRepository.getForestWeather()
            forestDayWeather = DataRepository.getForestDayWeather()
        }
        lastUpDataTimeStamp = System.currentTimeMillis()
    }

    fun upDataByTiming() {
        if (System.currentTimeMillis() - lastUpDataTimeStamp > 1000 * 60 * 10) {
            mainScope.launch {
                init()
            }
        }
    }

    fun getCountry(): String {
        return forestWeather?.city?.name ?: "NULL"
    }

    fun getTemperature(): String {
        return getTemperature(weather?.main?.temp, WeatherUnit.C).roundToInt().toString()
    }

    fun getTemperature(temp: Double?, unit: WeatherUnit): Double {
        if (temp == null) return 0.0
        return if (unit == WeatherUnit.C) {
            temp - 273.15
        } else {
            1.8 * (temp - 273) + 32
        }
    }

    fun getTemperatureHL(): String {
        var h = getTemperature(
            forestWeather?.getDailyMaxTemp(
                TimeUtil.getDateToString(
                    System.currentTimeMillis(),
                    "yyyy-MM-dd"
                )
            ), WeatherUnit.C
        ).roundToInt().toString()
        var l = getTemperature(
            forestWeather?.getDailyMinTemp(
                TimeUtil.getDateToString(
                    System.currentTimeMillis(),
                    "yyyy-MM-dd"
                )
            ), WeatherUnit.C
        ).roundToInt().toString()
        return "H:${h}° L:${l}°"
    }

    fun getWeatherDes(): String {
        return weather?.weather?.getOrNull(0)?.description ?: ""
    }

    fun getWeatherIcon(): Int {
        return weather?.weather?.getOrNull(0)?.icon?.toWeatherIcon() ?: R.drawable.ic_01d
    }

    fun getForestWeather(): List<ForestWeather.ListDTO> {
        return forestWeather?.list?.subList(0, 6) ?: arrayListOf()
    }


    fun getForestDayWeather(): List<ForestDayWeather.ListDay> {
        return forestDayWeather?.list?.subList(0, 4) ?: arrayListOf()
    }
}

fun String.toWeatherIcon(): Int {
    return when (this) {
        "01n" -> R.drawable.ic_01n
        "02d" -> R.drawable.ic_02d
        "02n" -> R.drawable.ic_02n
        "03d" -> R.drawable.ic_03d
        "03n" -> R.drawable.ic_03n
        "04d" -> R.drawable.ic_04d
        "04n" -> R.drawable.ic_04n
        "09d" -> R.drawable.ic_09d
        "09n" -> R.drawable.ic_09n
        "10d" -> R.drawable.ic_10d
        "10n" -> R.drawable.ic_10n
        "11d" -> R.drawable.ic_11d
        "11n" -> R.drawable.ic_11n
        "13d" -> R.drawable.ic_13d
        "13n" -> R.drawable.ic_13n
        "50d" -> R.drawable.ic_50d
        "50n" -> R.drawable.ic_50n
        else -> R.drawable.ic_01d
    }
}