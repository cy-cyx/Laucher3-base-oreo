package com.lambdaweather.adapter

import com.android.launcher3.databinding.ItemHomeTravelBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lambdaweather.data.model.AdvModel
import com.lambdaweather.utils.gone
import com.lambdaweather.utils.visible

class HomeTravelAdapter(layoutResId: Int) :
    BaseQuickAdapter<AdvModel, BaseViewHolder>(layoutResId) {
    override fun convert(holder: BaseViewHolder, item: AdvModel) {
        val binding = ItemHomeTravelBinding.bind(holder.itemView)
        binding.ivIcon.setImageResource(item.icon)
        binding.tvDes.text = item.name
        if (getItemPosition(item) == data.size - 1) {
            binding.tvLine.gone()
        } else {
            binding.tvLine.visible()
        }
    }
}