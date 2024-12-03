package com.theme.lambda.launcher.ui.news.adpater

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.R
import com.android.launcher3.databinding.ItemNewDetailsTopBinding
import com.lambda.news.data.model.News
import com.lambda.common.utils.GlideUtil

class NewDetailsTopViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    val viewBinding = ItemNewDetailsTopBinding.bind(view)

    fun bind(new: News) {
        new?.let {
            viewBinding.authorTv.text = it.author
            viewBinding.titleTv.text = it.title
            viewBinding.timeTv.text = it.publishDate
            it.image.getOrNull(0)?.let {
                GlideUtil.load(
                    viewBinding.logoIv,
                    it,
                    placeholder = R.drawable.ic_news_ph
                )
            }
        }
    }
}