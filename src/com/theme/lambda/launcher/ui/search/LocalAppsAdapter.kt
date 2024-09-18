package com.theme.lambda.launcher.ui.search

import com.android.launcher3.R
import com.android.launcher3.databinding.ItemSearchHistoryBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lambda.common.utils.utilcode.util.AppUtils
import com.lambda.common.utils.utilcode.util.ConvertUtils
import com.lambda.common.utils.utilcode.util.ImageUtils
import com.lambda.common.utils.utilcode.util.SizeUtils

class LocalAppsAdapter :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_local_apps) {
    override fun convert(holder: BaseViewHolder, item: String) {
        ItemSearchHistoryBinding.bind(holder.itemView).apply {
            tv.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                ConvertUtils.bitmap2Drawable(
                    ImageUtils.scale(
                        ConvertUtils.drawable2Bitmap(AppUtils.getAppIcon(item)),
                        SizeUtils.dp2px(44f),
                        SizeUtils.dp2px(44f)
                    )
                ),
                null,
                null
            )
            tv.text = AppUtils.getAppName(item)
        }
    }
}