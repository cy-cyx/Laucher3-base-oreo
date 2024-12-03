package com.lambda.news.ui.newslist.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lambda.common.ad.AdName
import com.lambda.common.ad.AdUtil
import com.lambda.common.ad.view.MRECBanner
import com.lambda.news.databinding.NewsItemAdMrecBinding
import com.lambda.news.ui.newslist.item.AdItem

class AdMRECViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private var viewBinding = NewsItemAdMrecBinding.bind(view)
    private var adItem: AdItem? = null

    fun bind(item: AdItem) {
        adItem = item
        if (adItem?.mercBanner == null) {
            adItem?.mercBanner = MRECBanner(AdUtil.getWapActivity()!!).apply {
                scenesName = AdName.news_list_mrec
            }
        }

        if (null != adItem?.mercBanner?.parent) {
            (adItem?.mercBanner?.parent as? ViewGroup)?.removeAllViews()
        }
        viewBinding.adFl.addView(adItem?.mercBanner)
    }

    fun onViewAttachedToWindow() {
        adItem?.mercBanner?.loadAd()
    }

    fun onViewDetachedFromWindow() {
        viewBinding.adFl.removeAllViews()
        adItem?.mercBanner?.destroy()
    }
}