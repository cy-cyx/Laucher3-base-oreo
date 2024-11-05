package com.theme.lambda.launcher.ui.news.adpater

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.R
import com.android.launcher3.ThemeManager
import com.android.launcher3.databinding.ItemNewsBinding
import com.theme.lambda.launcher.data.model.News
import com.theme.lambda.launcher.statistics.EventName
import com.theme.lambda.launcher.statistics.EventUtil
import com.theme.lambda.launcher.ui.news.NewDetailsActivity
import com.theme.lambda.launcher.utils.GlideUtil

class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    var viewBinding = ItemNewsBinding.bind(view)

    fun bind(data: News) {
        data.image.getOrNull(0)?.let {
            GlideUtil.load(
                viewBinding.iconIv,
                it,
                1000,
                1000,
                placeholder = R.drawable.ic_news_ph
            )
        }
        viewBinding.titleTv.text = data.title
        viewBinding.timeTv.text = data.publishDate
        viewBinding.root.setOnClickListener {
            if (ThemeManager.getThemeManagerIfExist()?.isPreviewMode == true) return@setOnClickListener
            NewDetailsActivity.start(viewBinding.root.context, data)
            EventUtil.logEvent(EventName.LNewsClick, Bundle().apply {
                putString("id", data.id)
            })
        }
    }
}