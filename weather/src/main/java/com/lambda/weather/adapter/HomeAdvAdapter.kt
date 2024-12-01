package com.lambdaweather.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lambda.weather.databinding.ItemHomeAdvBinding
import com.lambdaweather.data.model.AdvModel

class HomeAdvAdapter (layoutResId : Int): BaseQuickAdapter<AdvModel, BaseViewHolder>(layoutResId) {
    override fun convert(holder: BaseViewHolder, item: AdvModel) {
        val binding = ItemHomeAdvBinding.bind(holder.itemView)
        binding.ivIcon.setImageResource(item.icon)
        binding.tvTitle.text = item.name
        binding.tvDes.text = item.content
    }
}