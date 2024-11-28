package com.theme.lambda.launcher.ui.search.adapter

import com.android.launcher3.R
import com.android.launcher3.RecommendAppManager
import com.android.launcher3.databinding.ItemYouMayLikeBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.theme.lambda.launcher.data.model.Offers
import com.lambda.common.utils.GlideUtil

class YourMayLikeAdapter :
    BaseQuickAdapter<Offers, BaseViewHolder>(R.layout.item_you_may_like) {
    override fun convert(holder: BaseViewHolder, item: Offers) {
        ItemYouMayLikeBinding.bind(holder.itemView).apply {
            titleTv.text = item.name
            GlideUtil.load(logoIv, item.iconUrl)
            root.setOnClickListener {
                RecommendAppManager.clickRecommendApp(item)
            }
        }
    }
}