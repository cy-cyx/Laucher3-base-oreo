package com.lambda.news.ui.detail.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lambda.news.databinding.NewsItemNewDetailsBinding

class NewDetailsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    val viewBinding = NewsItemNewDetailsBinding.bind(view)

    fun bind(text: String) {
        viewBinding.contentTv.text = text
    }
}