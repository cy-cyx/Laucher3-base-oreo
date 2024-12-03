package com.theme.lambda.launcher.data

import androidx.room.Room
import com.android.launcher3.BuildConfig
import com.lambda.common.http.RequestParam
import com.lambdaweather.utils.WeatherUtils
import com.lambdaweather.utils.toCustomInt
import com.theme.lambda.launcher.data.api.AppApi
import com.lambda.common.net.RetrofitUtil
import com.theme.lambda.launcher.data.model.ForestDayWeather
import com.theme.lambda.launcher.data.model.ForestWeather
import com.lambda.news.data.model.NewResult
import com.theme.lambda.launcher.data.model.ResResult
import com.theme.lambda.launcher.data.model.ThemeRes
import com.theme.lambda.launcher.data.model.Weather
import com.lambda.common.utils.CommonUtil
import com.lambda.common.utils.TimeUtil
import java.util.Locale

object DataRepository {

    private val service by lazy {
        RetrofitUtil.retrofit.create(AppApi::class.java)
    }

    private val db by lazy {
        Room.databaseBuilder(
            CommonUtil.appContext!!,
            LauncherDataBase::class.java,
            "launcher_um_db"
        ).build()
    }

    suspend fun getNewData(page: Long, categories: String = ""): NewResult? {
        try {
            return service.getNewData(
                Locale.getDefault().country,
                Locale.getDefault().language,
                "$page",
                "25",
                categories,
                "${TimeUtil.getOldDate(-365)}",
                "publish_date,desc",
                "true"

            ).d
        } catch (e: Exception) {
        }
        return null
    }

    suspend fun getResource(page: Long, tag: String): ResResult? {

        try {
            val params: MutableMap<String, String> =
                mutableMapOf(
                    "r_tag" to tag,
                    "page" to page.toString(),
                    "page_size" to "20",
                    "sort_by" to "2",
                    "r_types" to "1"
                )
            return service.getResource(
                RequestParam.Builder().build().buildParam(params, BuildConfig.SECRET_KEY, false)
            ).d
        } catch (e: Exception) {
        }
        return null
    }

    fun insertDownLoadThemeIntoDb(themeRes: ThemeRes) {
        db.themeResDao().insert(themeRes)
    }

    fun getDownLoadThemeRecord(): List<ThemeRes> {
        return db.themeResDao().getDownLoadThemeResList()
    }

    suspend fun getWeather(): Weather? {
        try {
            val locationModel = WeatherUtils.getSelectLocation()
            return service.weather(
                locationModel?.lat?.toCustomInt() ?: "40.6643",
                locationModel?.lon?.toCustomInt() ?: "-73.9385",
                Locale.getDefault().language
            )
        } catch (e: Exception) {
        }
        return null
    }

    suspend fun getForestWeather(): ForestWeather? {
        try {
            val locationModel = WeatherUtils.getSelectLocation()
            return service.forecastWeather(
                locationModel?.lat?.toCustomInt() ?: "40.6643",
                locationModel?.lon?.toCustomInt() ?: "-73.9385",
                Locale.getDefault().language
            )
        } catch (e: Exception) {

        }
        return null
    }

    suspend fun getForestDayWeather(): ForestDayWeather? {
        try {
            val locationModel = WeatherUtils.getSelectLocation()
            val map = mapOf(
                "lat" to (locationModel?.lat?.toCustomInt() ?: "40.6643"),
                "lon" to (locationModel?.lon?.toCustomInt() ?: "-73.9385"),
                "lang" to Locale.getDefault().language,
                "cnt" to "7",
                "host" to "api.openweathermap.org"
            )
            return service.forecastDay7Weather(
                map
            )
        } catch (e: Exception) {
        }
        return null
    }
}