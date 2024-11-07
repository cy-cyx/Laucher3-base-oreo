package com.theme.lambda.launcher.ui.search.adapter

import com.android.launcher3.R
import com.android.launcher3.databinding.ItemNetUrlBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.theme.lambda.launcher.utils.CommonUtil

class NetUrlAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_net_url) {
    override fun convert(holder: BaseViewHolder, item: String) {
        ItemNetUrlBinding.bind(holder.itemView).apply {
            urlTv.text = item
            urlTv.setOnClickListener {
                CommonUtil.openWebView(CommonUtil.appContext!!, item)
            }
        }
    }
}