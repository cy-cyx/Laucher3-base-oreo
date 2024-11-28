package com.theme.lambda.launcher.ui.news.adpater

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.databinding.ItemAdBinding
import com.lambda.common.ad.AdName

class AdViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    var viewBinding = ItemAdBinding.bind(view)

    init {
        viewBinding.adView.loadAd(AdName.theme_new_nat)
    }

    fun onViewAttachedToWindow() {
        viewBinding.adView.onViewAttachedToWindow()
    }

    fun onViewDetachedFromWindow() {
        viewBinding.adView.onViewDetachedFromWindow()
    }
}