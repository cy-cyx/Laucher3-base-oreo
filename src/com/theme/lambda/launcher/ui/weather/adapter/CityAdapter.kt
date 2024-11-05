package com.theme.lambda.launcher.ui.weather.adapter

import com.android.launcher3.R
import com.android.launcher3.databinding.ItemCityBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lambdaweather.data.model.CityModel

class CityAdapter :
    BaseQuickAdapter<CityModel, BaseViewHolder>(R.layout.item_city) {
    override fun convert(holder: BaseViewHolder, item: CityModel) {
        ItemCityBinding.bind(holder.itemView).apply {
            tv.text = item.name
        }
    }
}