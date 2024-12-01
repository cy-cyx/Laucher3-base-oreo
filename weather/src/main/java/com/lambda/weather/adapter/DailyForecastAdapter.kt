package com.lambdaweather.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lambda.weather.databinding.ItemDailyWeatherBinding
import com.lambdaweather.data.model.ListDay
import com.lambdaweather.utils.GlideUtil
import com.lambdaweather.utils.TimeUtils
import com.lambdaweather.utils.WeatherUtils
import kotlin.math.roundToInt

class DailyForecastAdapter(layoutResId: Int) :
    BaseQuickAdapter<ListDay, BaseViewHolder>(layoutResId) {
    override fun convert(holder: BaseViewHolder, item: ListDay) {
        val binding = ItemDailyWeatherBinding.bind(holder.itemView)
        GlideUtil.loadResImage(
            context,
            WeatherUtils.getIconUrl(item.weather.get(0)?.icon), binding.ivIcon
        )
        binding.tvWeek.text = TimeUtils.getDayOfWeek(
            TimeUtils.longToDate(
                item.dt * 1000L,
                TimeUtils.TIME_TYPE2
            )
        )
        binding.tvWeather.text = item.weather.get(0)?.main.toString()
        binding.tvTemp.text = "${
            WeatherUtils.getTemp(
                item.temp.min
            ).roundToInt().toString() + WeatherUtils.getTempUnit()
        }" +
                " / " +
                "${
                    WeatherUtils.getTemp(
                        item.temp.max
                    ).roundToInt().toString() + WeatherUtils.getTempUnit()
                }"
    }
}