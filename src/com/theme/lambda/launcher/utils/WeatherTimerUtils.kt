package com.theme.lambda.launcher.utils

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.launcher3.R
import com.lambdaweather.data.model.MyLocationModel
import com.lambdaweather.data.model.WeatherModel
import com.lambdaweather.data.repository.AppRepositorySource
import com.lambdaweather.utils.WeatherUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject
import java.util.Date
import java.util.Timer
import java.util.TimerTask
import kotlin.math.roundToInt

/**
 * 用于刷新天气组件的工具类
 */
object WeatherTimerUtils {
    private var mTimer: Timer? = null
    private var mCallback: ((String, WeatherModel) -> Unit)? = null

    @JvmStatic
    fun schedule(callback: (String, WeatherModel) -> Unit) {
        mCallback = callback
        if (mTimer == null) {
            mTimer = Timer().apply {
                schedule(
                    object : TimerTask() {
                        override fun run() {
                            getIpLocation()
                        }
                    },
                    0L,
                    60 * 60 * 1000L
                )
            }
        }
        // 初始化强行设置一次
        getIpLocation()
    }

    fun getIpLocation() {
        val locationModel = WeatherUtils.getSelectLocation()
        if (null != locationModel) {
            getWeather(locationModel)
        } else {
            getWeather(
                MyLocationModel(
                    "40.6643",
                    "-73.9385",
                    locality = "New York",
                    thoroughfare = null
                )
            )
        }
    }


    private fun getWeather(locationModel: MyLocationModel) {
        //获取天气
        CoroutineScope(Dispatchers.IO).launch {
            val appRepositorySource: AppRepositorySource by inject(
                AppRepositorySource::class.java
            )
            withContext(Dispatchers.IO) {
                async {
                    appRepositorySource.getWeather(locationModel.lat, locationModel.lon).collect {
                        it.data?.let { data ->
                            mCallback?.invoke(
                                locationModel.getLocalName(),
                                data
                            )
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @JvmStatic
    fun update(view: View, local: String, model: WeatherModel) {
        view.findViewById<TextView>(R.id.tv_temp).text =
            WeatherUtils.getTemp(model.main?.temp).roundToInt()
                .toString() + WeatherUtils.getTempUnit()
        view.findViewById<TextView>(R.id.tv_location).text =
            "$local ${TimeUtil.getDateToString((model.dt ?: 0) * 1000L, "M/d")} ${
                TimeUtil.getDayOfWeek(
                    Date(
                        model.dt!! * 1000L
                    )
                )
            }"
        view.findViewById<ImageView>(R.id.iv_icon)
            .setImageResource(WeatherUtils.getIconUrl(model.weather?.get(0)?.icon))
    }
}