package com.lambda.weather.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lambda.weather.R
import com.lambda.weather.databinding.ItemCityBinding
import com.lambdaweather.data.model.CityModel

class CityAdapter :
    BaseQuickAdapter<CityModel, BaseViewHolder>(R.layout.item_city) {
    override fun convert(holder: BaseViewHolder, item: CityModel) {
        ItemCityBinding.bind(holder.itemView).apply {
            tv.text = item.name
        }
    }
}