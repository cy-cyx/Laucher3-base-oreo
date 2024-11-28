package com.theme.lambda.launcher.ui.news.adpater

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.databinding.ItemAdMrecBinding
import com.lambda.common.ad.AdName
import com.lambda.common.ad.AdUtil
import com.lambda.common.ad.view.MRECBanner

class AdMRECViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        val mrecAdBanner: MRECBanner by lazy {
            MRECBanner(AdUtil.getWapActivity()!!).apply {
                scenesName = AdName.news_list_mrec
                loadAd()
            }
        }
    }

    var viewBinding = ItemAdMrecBinding.bind(view)

    fun onViewAttachedToWindow() {
        if (null != mrecAdBanner.parent) {
            (mrecAdBanner.parent as ViewGroup).removeAllViews()
        }
        viewBinding.adFl.addView(mrecAdBanner)
        mrecAdBanner.loadAd()
    }

    fun onViewDetachedFromWindow() {
        viewBinding.adFl.removeAllViews()
    }
}