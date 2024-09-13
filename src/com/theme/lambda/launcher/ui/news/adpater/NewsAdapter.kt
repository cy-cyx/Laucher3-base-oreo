package com.theme.lambda.launcher.ui.news.adpater

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.databinding.ItemNewsBinding
import com.theme.lambda.launcher.data.model.News
import com.theme.lambda.launcher.utils.CommonUtil

class NewsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var lastVisibleItem = 0

    private val data = ArrayList<News>()

    @SuppressLint("NotifyDataSetChanged")
    fun upData(d: ArrayList<News>) {
        data.clear()
        data.addAll(d)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NewsViewHolder(ItemNewsBinding.inflate(LayoutInflater.from(parent.context)).root.apply {
            layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonUtil.dp2px(250f))
        })
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NewsViewHolder) {
            holder.bind(data[position])
        }
    }

}