package com.theme.lambda.launcher.ui.search.adapter

import com.android.launcher3.R
import com.android.launcher3.ThemeIconMappingV2
import com.android.launcher3.databinding.ItemRecentAppsBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lambda.common.utils.utilcode.util.AppUtils

class RecentAppsAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_recent_apps) {

    override fun convert(holder: BaseViewHolder, item: String) {
        ItemRecentAppsBinding.bind(holder.itemView).apply {
            iv.setImageBitmap(ThemeIconMappingV2.getIconBitmapIfNeedAsyn(iv, item, ""))
            tv.text = AppUtils.getAppName(item)
        }
    }
}