package com.lambda.news.ui.detail.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lambda.common.ad.AdName
import com.lambda.common.ad.view.MRECBanner
import com.lambda.news.databinding.NewsItemNewDetailsAdBinding
import com.lambda.news.ui.detail.item.NewDetailsAdItem

class NewDetailsAdViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    val viewBinding = NewsItemNewDetailsAdBinding.bind(view)

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