package com.lambdaweather.utils

import com.android.launcher3.R
import com.google.gson.reflect.TypeToken
import com.lambda.common.utils.utilcode.util.ResourceUtils
import com.lambdaweather.LambdaWeather
import com.lambdaweather.data.model.MyLocationModel
import com.lambdaweather.view.dynamicweather.BaseDrawer
import java.util.Calendar

object WeatherUtils {

    fun getTemp(temp: Double?): Double {
        if (temp == null) return 0.0
        return if (getTempUnit() == LambdaWeather.application.getString(R.string.temp_unit)) {
            temp - 273.15
        } else {
            1.8 * (temp - 273) + 32
        }
    }

    fun getIconUrl(icon: String?): Int {
        if (null == icon) ResourceUtils.getDrawableIdByName( "ic_01d")
        return ResourceUtils.getDrawableIdByName("ic_$icon")
    }

    fun getLocationList(): List<MyLocationModel> {
        val locationList = SharedPreferencesManager.getInstance(LambdaWeather.application).getString("locationList","")
        val type = object : TypeToken<List<MyLocationModel>>() {}.type
        val temp = GsonUtil.mGson.fromJson<List<MyLocationModel>>(locationList, type)
        return temp ?: mutableListOf()
    }

    fun updateLocationList(list: MutableList<MyLocationModel>) {
        val locationList = SharedPreferencesManager.getInstance(LambdaWeather.application).getString("locationList","")
        SharedPreferencesManager.getInstance(LambdaWeather.application).putString("locationList",GsonUtil.toJson(list))
    }

    fun getSelectLocation(): MyLocationModel? {
        val list: List<MyLocationModel> = getLocationList()
        list.forEach {
            if (it.isSelect) {
                return it
            }
        }
        if (list.isNotEmpty()) {
            return list.get(0)
        }
        return null
    }

    fun getMyLocation(): MyLocationModel? {
        val list: List<MyLocationModel> = getLocationList()
        list.forEach {
            if (it.isLocation) {
                return it
            }
        }
        return null
    }

    fun updateLocationData(model: MyLocationModel) {

        val list: MutableList<MyLocationModel> = getLocationList() as MutableList<MyLocationModel>
        var isUpdate = false
        if (model.isSelect) {
            list.forEach { it2 ->
                it2.isSelect = false
            }
        }
        list.forEach {
            if (it.lat == model.lat && it.lon == model.lon) {
                it.weather = model.weather
                it.isSelect = model.isSelect
                it.temp = model.temp
                it.tempLowUp = model.tempLowUp
                isUpdate = true
            } else {
                if (model.isSelect) {
                    it.isSelect = false
                }
            }
        }

        if (!isUpdate) {
            list.add(model)
        }
        updateLocationList(list)
    }

    fun deleteLocationList(model: MyLocationModel) {
        val list: MutableList<MyLocationModel> = getLocationList() as MutableList<MyLocationModel>
        var isSelect = false
        var temp: MyLocationModel? = null
        list.forEach {
            if (it.lat == model.lat && it.lon == model.lon) {
                isSelect = model.isSelect
                temp = it
            }
        }
        list.remove(temp)
        if (isSelect && list.isNotEmpty()) {
            list.get(0).isSelect = true
        }
        updateLocationList(list)
    }

    fun getWeatherType(weatherIcon: String): BaseDrawer.Type {
        return when (weatherIcon) {
            "01d" -> BaseDrawer.Type.CLEAR_D
            "02d" -> BaseDrawer.Type.CLOUDY_D
            "03d" -> BaseDrawer.Type.CLOUDY_D
            "04d" -> BaseDrawer.Type.CLOUDY_D
            "09d" -> BaseDrawer.Type.RAIN_D
            "10d" -> BaseDrawer.Type.RAIN_D
            "11d" -> BaseDrawer.Type.RAIN_THUNDER_D
            "13d" -> BaseDrawer.Type.RAIN_SNOW_D
            "50d" -> BaseDrawer.Type.HAZE_D
            "01n" -> BaseDrawer.Type.CLEAR_N
            "02n" -> BaseDrawer.Type.CLOUDY_N
            "03n" -> BaseDrawer.Type.CLOUDY_N
            "04n" -> BaseDrawer.Type.CLOUDY_N
            "09n" -> BaseDrawer.Type.RAIN_N
            "10n" -> BaseDrawer.Type.RAIN_N
            "11n" -> BaseDrawer.Type.RAIN_THUNDER_N
            "13n" -> BaseDrawer.Type.RAIN_SNOW_N
            "50n" -> BaseDrawer.Type.HAZE_N
            else -> BaseDrawer.Type.CLEAR_D
        }
    }

    fun getWeatherTypeByWeather(weather: String): BaseDrawer.Type {
        return when (weather) {
            "Clear" -> BaseDrawer.Type.CLEAR_D
            "Clouds" -> BaseDrawer.Type.CLOUDY_D
            "Rain" -> BaseDrawer.Type.RAIN_D
            "Drizzle" -> BaseDrawer.Type.RAIN_D
            "Thunderstorm" -> BaseDrawer.Type.RAIN_THUNDER_D
            "Snow" -> BaseDrawer.Type.RAIN_SNOW_D
            "Mist", "Smoke", "Haze", "Dust", "Fog", "Sand", "Dust", "Ash", "Squall", "Tornado" -> BaseDrawer.Type.HAZE_D
            else -> BaseDrawer.Type.CLEAR_D
        }
    }

    fun getTempUnit(): String {
        var tempUnit = SharedPreferencesManager.getInstance(LambdaWeather.application).getString("tempUnit", LambdaWeather.application.getString(R.string.temp_unit))
        return tempUnit!!
    }

    fun setTempUnit(type: Int) {
        if (type == 1) {
            SharedPreferencesManager.getInstance(LambdaWeather.application).putString("tempUnit", LambdaWeather.application.getString(R.string.temp_unit))
        } else {
            SharedPreferencesManager.getInstance(LambdaWeather.application).getString("tempUnit", LambdaWeather.application.getString(R.string.temp_unit_f))
        }
    }

    fun isColdSeason(time: Long): Boolean {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        val month = calendar.get(Calendar.MONTH) + 1
        // 判断当前月份是否位于容易感冒的季节（根据地区而定）
        return month == 1 || month == 2 || month == 12
    }
}