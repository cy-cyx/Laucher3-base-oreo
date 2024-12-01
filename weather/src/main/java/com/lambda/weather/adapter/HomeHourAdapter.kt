package com.lambdaweather.adapter

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lambda.weather.R
import com.lambda.weather.databinding.ItemHomeHourBinding
import com.lambdaweather.data.model.ForestWeatherModel
import com.lambdaweather.utils.GlideUtil
import com.lambdaweather.utils.TimeUtils
import com.lambdaweather.utils.WeatherUtils

class HomeHourAdapter(layoutResId: Int) :
    BaseQuickAdapter<ForestWeatherModel.ListDTO, BaseViewHolder>(layoutResId) {
    override fun convert(holder: BaseViewHolder, item: ForestWeatherModel.ListDTO) {
        val binding = ItemHomeHourBinding.bind(holder.itemView)
        GlideUtil.loadResImage(
            context,
            WeatherUtils.getIconUrl(item.weather?.get(0)?.icon), binding.ivWeather
        )

        var textStr = if (item.dtTxt!!.contains("00:00:00")) {
            TimeUtils.getDateToString(item.dt!! * 1000L, "h a")
        } else {
            TimeUtils.getDateToString(item.dt!! * 1000L, "h a")
        }

        binding.tvName.text = textStr
        if (getItemPosition(item) % 2 == 0) {
            binding.ivHourBg.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.color_forecast1
                )
            )
        } else {
            binding.ivHourBg.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.color_forecast2
                )
            )
        }

    }
}