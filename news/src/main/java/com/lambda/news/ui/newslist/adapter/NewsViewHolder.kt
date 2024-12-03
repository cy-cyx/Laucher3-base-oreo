package com.lambda.news.ui.newslist.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lambda.common.utils.GlideUtil
import com.lambda.news.R
import com.lambda.news.data.model.News
import com.lambda.news.databinding.NewsItemNewBinding

class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val viewBinding = NewsItemNewBinding.bind(view)

    fun bind(news: News) {
        viewBinding.autherTv.text = news.source
        if (news.image.isNotEmpty()) {
            GlideUtil.load(
                viewBinding.logoIv,
                news.image.first(),
                placeholder = R.drawable.ic_news_ph
            )
        }
        if (news.sourceIcon.isNotBlank()){
            GlideUtil.load(
                viewBinding.iconIv,
                news.sourceIcon
            )
        }
        viewBinding.positionTv.text = news.sourceCountry
        viewBinding.timeTv.text = news.publishDate
        viewBinding.titleTv.text = news.title
    }
}