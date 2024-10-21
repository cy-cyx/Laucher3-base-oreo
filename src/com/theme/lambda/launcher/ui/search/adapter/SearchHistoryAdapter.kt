package com.theme.lambda.launcher.ui.search.adapter

import com.android.launcher3.R
import com.android.launcher3.databinding.ItemSearchHistoryBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class SearchHistoryAdapter :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_search_history) {
    override fun convert(holder: BaseViewHolder, item: String) {
        ItemSearchHistoryBinding.bind(holder.itemView).apply {
            tv.text = item
        }
    }
}