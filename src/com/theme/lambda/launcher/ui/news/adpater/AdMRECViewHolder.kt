package com.theme.lambda.launcher.ui.news.adpater

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.databinding.ItemAdMrecBinding
import com.theme.lambda.launcher.ad.AdName
import com.theme.lambda.launcher.ad.AdUtil
import com.theme.lambda.launcher.ad.view.MRECBanner

class AdMRECViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        val mrecAdBanner: MRECBanner by lazy {
            MRECBanner(AdUtil.getWapActivity()!!).apply {
                scenesName = AdName.theme_new_mrec
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