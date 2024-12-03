package com.lambda.news.ui.detail.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lambda.news.data.model.News
import com.lambda.common.utils.GlideUtil
import com.lambda.news.R
import com.lambda.news.databinding.NewsItemNewDetailsTopBinding

class NewDetailsTopViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    val viewBinding = NewsItemNewDetailsTopBinding.bind(view)

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