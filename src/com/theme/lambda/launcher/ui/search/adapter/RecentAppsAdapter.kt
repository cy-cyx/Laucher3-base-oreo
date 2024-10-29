package com.theme.lambda.launcher.ui.search.adapter

import com.android.launcher3.R
import com.android.launcher3.ThemeIconMapping
import com.android.launcher3.databinding.ItemRecentAppsBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lambda.common.utils.utilcode.util.AppUtils
import com.lambda.common.utils.utilcode.util.ConvertUtils

class RecentAppsAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_recent_apps) {

    override fun convert(holder: BaseViewHolder, item: String) {
        ItemRecentAppsBinding.bind(holder.itemView).apply {
            iv.setImageBitmap(
                ThemeIconMapping.getThemeBitmap(holder.itemView.context, item, "")
                    ?: ConvertUtils.drawable2Bitmap(AppUtils.getAppIcon(item))
            )
            tv.text = AppUtils.getAppName(item)
        }
    }
}