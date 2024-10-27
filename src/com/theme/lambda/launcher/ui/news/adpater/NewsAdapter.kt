package com.theme.lambda.launcher.ui.news.adpater

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.databinding.ItemAdBinding
import com.android.launcher3.databinding.ItemNewsBinding
import com.theme.lambda.launcher.ui.news.item.AdItem
import com.theme.lambda.launcher.base.BaseItem
import com.theme.lambda.launcher.ui.news.item.NewsItem
import com.theme.lambda.launcher.utils.CommonUtil

class NewsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val viewTypeNew = 1
        val viewTypeAd = 2
    }

    private val data = ArrayList<BaseItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun upData(d: ArrayList<BaseItem>) {
        data.clear()
        data.addAll(d)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val baseItem = data[position]
        when (baseItem) {
            is NewsItem -> {
                return viewTypeNew
            }

            is AdItem -> {
                return viewTypeAd
            }
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            viewTypeAd ->{
                return AdViewHolder(ItemAdBinding.inflate(LayoutInflater.from(parent.context)).root.apply {
                    layoutParams =
                        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonUtil.dp2px(260f))
                })
            }
            else ->{
                return NewsViewHolder(ItemNewsBinding.inflate(LayoutInflater.from(parent.context)).root.apply {
                    layoutParams =
                        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonUtil.dp2px(250f))
                })
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is NewsViewHolder ->{
                (data[position] as? NewsItem)?.let {
                    holder.bind(it.new)
                }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        (holder as? AdViewHolder)?.onViewAttachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        (holder as? AdViewHolder)?.onViewDetachedFromWindow()
    }
}