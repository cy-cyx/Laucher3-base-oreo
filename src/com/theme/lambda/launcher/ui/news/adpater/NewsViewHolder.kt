package com.theme.lambda.launcher.ui.news.adpater

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.databinding.ItemNewsBinding
import com.theme.lambda.launcher.data.model.News
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
            )
        }
        viewBinding.titleTv.text = data.title
        viewBinding.timeTv.text = data.publishDate
        viewBinding.root.setOnClickListener {
            NewDetailsActivity.start(viewBinding.root.context,data)
        }
    }
}