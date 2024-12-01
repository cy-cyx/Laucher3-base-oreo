package com.lambda.weather

import android.location.Location
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lambdaweather.data.Resource
import com.lambdaweather.data.model.AirModel
import com.lambdaweather.data.model.CityModel
import com.lambdaweather.data.model.ForestDayWeatherModel
import com.lambdaweather.data.model.ForestWeatherModel
import com.lambdaweather.data.model.IpModel
import com.lambdaweather.data.model.MyLocationModel
import com.lambdaweather.data.model.WeatherModel
import com.lambdaweather.data.repository.AppRepositorySource
import com.lambdaweather.utils.TimeUtils
import com.lambdaweather.utils.WeatherUtils
import com.lambdaweather.view.dynamicweather.BaseDrawer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.roundToInt

class AppViewModel(appRepositorySource: AppRepositorySource) : ApiViewModel(appRepositorySource) {
    val weather = MutableLiveData<Resource<WeatherModel>>()
    val mLocation: MutableLiveData<MyLocationModel?> = MutableLiveData<MyLocationModel?>()
    val forecastWeather = MutableLiveData<Resource<ForestWeatherModel>>()
    val forecastHourWeather = MutableLiveData<Resource<ForestWeatherModel>>()
    val forecastDay7Weather = MutableLiveData<Resource<ForestDayWeatherModel>>()
    val cityList = MutableLiveData<List<MyLocationModel>>()
    val forecastAir = MutableLiveData<Resource<AirModel>>()
    val onBackFragment = MutableLiveData<Int>()
    val onBackHomeFragment = MutableLiveData<Int>()
    var homeVisible = false
    private var onBackFragmentCount = 0
    val bgColor = MutableLiveData<Int>().apply {
        value = R.drawable.shape_sunny_bg
    }

    val weatherType = MutableLiveData<BaseDrawer.Type>().apply {
        value = BaseDrawer.Type.DEFAULT
    }

    private var updateLocation: MyLocationModel? = null

    fun getWeather() {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                async {
                    appRepositorySource.getWeather(
                        mLocation.value?.lat.toString(),
                        mLocation.value?.lon.toString()
                    ).collect {

                        weather.value = it
                        updateBgColor(it.data?.weather?.get(0)?.description.toString())
                        updateWeatherType(it.data?.weather?.get(0)?.icon.toString())
                        updateLocation?.let { it1 ->
                            it1.weather = it.data?.weather?.get(0)?.main.toString()
                            it1.temp = WeatherUtils.getTemp(it.data?.main?.temp).roundToInt()
                                .toString() + WeatherUtils.getTempUnit()
                        }
                    }
                }

                async {
                    appRepositorySource.getForecastHourWeather(
                        mLocation.value?.lat.toString(),
                        mLocation.value?.lon.toString()
                    ).collect {
                        forecastHourWeather.value = it
                        updateLocation?.let { it1 ->
                            it1.tempLowUp = WeatherUtils.getTemp(
                                it.data?.getDailyMaxTemp(
                                    TimeUtils.getDateToString(
                                        TimeUtils.getCurrentTimeMillis(),
                                        TimeUtils.TIME_TYPE
                                    )
                                )
                            ).roundToInt().toString() + WeatherUtils.getTempUnit() +
                                    " / " + WeatherUtils.getTemp(
                                it.data?.getDailyMinTemp(
                                    TimeUtils.getDateToString(
                                        TimeUtils.getCurrentTimeMillis(),
                                        TimeUtils.TIME_TYPE
                                    )
                                )
                            ).roundToInt().toString() + WeatherUtils.getTempUnit()
                        }
                    }
                }

            }
            updateLocation?.let { it1 ->
                updateLocationWeather(it1)
            }
        }
    }

    fun getForecastAir() {
        viewModelScope.launch {
            appRepositorySource.getForecastAirPollution(
                mLocation.value?.lat.toString(),
                mLocation.value?.lon.toString()
            ).collect {
                forecastAir.value = it
            }
        }
    }

    fun getForecastHour() {
        viewModelScope.launch {
            appRepositorySource.getForecastHourWeather(
                mLocation.value?.lat.toString(),
                mLocation.value?.lon.toString()
            ).collect {
                forecastHourWeather.value = it
            }
        }
    }


    fun getForecastDay7Hour() {
        viewModelScope.launch {
            appRepositorySource.getForecastDay7Weather(
                mLocation.value?.lat.toString(),
                mLocation.value?.lon.toString()
            ).collect {
                forecastDay7Weather.value = it
            }
        }
    }

    fun updateLocation() {
        val temp = WeatherUtils.getSelectLocation()
        if (temp?.lat == mLocation.value?.lat && temp?.lon == mLocation.value?.lon && temp != null) {
            return
        }
        mLocation.value = temp
        updateLocation = mLocation.value?.copy()
    }

    fun updateRefreshLocation() {
        val temp = WeatherUtils.getSelectLocation()
        mLocation.value = temp
        updateLocation = mLocation.value?.copy()
    }

    fun isHasLocation(): Boolean {
        return !TextUtils.isEmpty(mLocation.value?.lat)
    }

    fun updateLocationWeather(model: MyLocationModel) {
        viewModelScope.launch {
            WeatherUtils.updateLocationData(model)
        }
    }

    fun getAllCity() {
        viewModelScope.launch {
            cityList.value = WeatherUtils.getLocationList()
        }
    }

    fun sortLocationList(list: MutableList<MyLocationModel>) {
        viewModelScope.launch {
            WeatherUtils.updateLocationList(list)
        }
    }

    fun delLocationWeather(model: MyLocationModel) {
        viewModelScope.launch {
            WeatherUtils.deleteLocationList(model)
        }
    }

    val ipModel = MutableLiveData<Resource<IpModel>>()

    fun getIp() {
        viewModelScope.launch {
            appRepositorySource.getIp(
            ).collect {
                ipModel.value = it
            }
        }
    }

    private fun updateBgColor(weather: String) {
        if (getCurrentTime()) {
            bgColor.value = R.drawable.shape_night_bg
        } else if (weather.contains("overcast", true)) {
            bgColor.value = R.drawable.shape_overcast_bg
        } else {
            bgColor.value = R.drawable.shape_sunny_bg
        }
    }

    private fun updateWeatherType(weatherIcon: String) {
        weatherType.value = WeatherUtils.getWeatherType(weatherIcon)
    }

    fun getCurrentTime(): Boolean {
        val sdf = SimpleDateFormat("HH")
        val hour: String = sdf.format(Date())
        val k = hour.toInt()
        return k in 0..5 || k in 18..23
    }

    val mLocationSuccess: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    fun locationBuild(location: Location?) {
        viewModelScope.launch {
            appRepositorySource.getGeoByLat(
                location?.latitude.toString(),
                location?.longitude.toString()
            ).collect { model ->
                var temp: MutableList<MyLocationModel> =
                    WeatherUtils.getLocationList() as MutableList<MyLocationModel>
                var originLocal: MyLocationModel? = null
                if (temp.isNotEmpty()) {
                    originLocal = temp[0]
                }
                originLocal?.let {
                    if (it.isLocation) {
                        temp.remove(originLocal)
                    }
                }
                val lTemp: CityModel? = if (model.data?.size == 0) CityModel().apply {
                    lat = location?.latitude
                    lon = location?.longitude
                    name = location?.provider
                } else model.data?.get(0)

                lTemp?.let {
                    temp.add(
                        0,
                        MyLocationModel(
                            it.lat.toString(),
                            it.lon.toString(),
                            it.name,
                            it.state,
                            isLocation = true,
                            isSelect = true,
                            country = it.country,
                        )
                    )
                    WeatherUtils.updateLocationList(temp)
                }
                mLocationSuccess.value = true
            }
        }
    }
}