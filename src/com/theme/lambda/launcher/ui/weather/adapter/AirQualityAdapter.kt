package com.lambdaweather.adapter

import android.content.res.ColorStateList
import com.android.launcher3.databinding.ItemAirQualityBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lambdaweather.data.model.Aqi
import com.lambdaweather.utils.AqiUtil

class AirQualityAdapter(layoutResId: Int) : BaseQuickAdapter<Aqi, BaseViewHolder>(layoutResId) {
    override fun convert(holder: BaseViewHolder, item: Aqi) {
        val binding = ItemAirQualityBinding.bind(holder.itemView)
        binding.tvDes.text = item.name
        binding.tvName.text = item.aqi.toInt().toString()
        binding.cvAll.setCardBackgroundColor(
            ColorStateList.valueOf(
                AqiUtil.getAqiColor(
                    context,
                    item.aqiIndex
                )
            )
        )
    }
}