package com.theme.lambda.launcher.utils

import android.annotation.SuppressLint
import android.content.Context
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

object TimerUtils {
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
                    0,
                    30 * 1000L
                )
            }
        }
    }

    private fun getIpLocation() {
        val locationModel = WeatherUtils.getSelectLocation()
        if (null != locationModel) {
            getWeather(locationModel)
            return
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
    fun update(context: Context, view: View, local: String, model: WeatherModel) {
        view.findViewById<TextView>(R.id.tv_temp).text =
            WeatherUtils.getTemp(model.main?.temp).roundToInt()
                .toString() + WeatherUtils.getTempUnit()
        view.findViewById<TextView>(R.id.tv_location).text =
            "$local ${TimeUtils.getDateToString(model.dt!! * 1000L, "M/d")} ${
                TimeUtils.getDayOfWeek(
                    Date(
                        model.dt!! * 1000L
                    )
                )
            }"

//        views.setImageViewResource(
//            R.id.iv_bg, /*when (model.main?.) {
//                WidgetModel.SUNNY -> R.drawable.shape_widget_sunny_bg
//                WidgetModel.OVERCAST -> R.drawable.shape_widget_overcast_bg
//                WidgetModel.NIGHT -> R.drawable.shape_widget_night_bg
//                else -> R.drawable.shape_widget_empty_bg
//            }*/
//            R.drawable.shape_widget_sunny_bg
//        )
        view.findViewById<ImageView>(R.id.iv_bg).setImageResource(R.drawable.shape_widget_night_bg)
        view.findViewById<ImageView>(R.id.iv_icon)
            .setImageResource(WeatherUtils.getIconUrl(model.weather?.get(0)?.icon))
    }
}