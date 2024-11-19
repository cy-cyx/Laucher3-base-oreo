package com.theme.lambda.launcher.ui.news.adpater

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.databinding.ItemNewDetailsBinding

class NewDetailsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    val viewBinding = ItemNewDetailsBinding.bind(view)

    fun bind(text: String) {
        viewBinding.contentTv.text = text
    }
}