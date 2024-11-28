package com.theme.lambda.launcher.ui.news.adpater

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.databinding.ItemNewDetailsAdBinding
import com.lambda.common.ad.AdName
import com.lambda.common.ad.view.MRECBanner
import com.theme.lambda.launcher.ui.news.item.NewDetailsAdItem

class NewDetailsAdViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    val viewBinding = ItemNewDetailsAdBinding.bind(view)

    fun bind(item: NewDetailsAdItem) {
        if (item.mrecBanner == null) {
            item.mrecBanner = MRECBanner(viewBinding.root.context)
            item.mrecBanner?.scenesName = AdName.news_detail_mrec
            item.mrecBanner?.bindLifecycle(viewBinding.root.context)
        }

        if (null != item.mrecBanner?.parent) {
            (item.mrecBanner?.parent as? ViewGroup)?.removeAllViews()
        }

        viewBinding.adContentFl.addView(item.mrecBanner)
        item.mrecBanner?.loadAd()
    }
}