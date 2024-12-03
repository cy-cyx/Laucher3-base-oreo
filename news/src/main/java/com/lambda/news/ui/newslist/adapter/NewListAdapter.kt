package com.lambda.news.ui.newslist.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.lambda.common.base.BaseItem
import com.lambda.common.utils.CommonUtil
import com.lambda.common.utils.noDoubleClick
import com.lambda.news.data.model.News
import com.lambda.news.databinding.NewsItemAdMrecBinding
import com.lambda.news.databinding.NewsItemNewBinding
import com.lambda.news.ui.newslist.item.AdItem
import com.lambda.news.ui.newslist.item.NewsItem

class NewListAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val data = ArrayList<BaseItem>()

    companion object {
        val viewTypeNew = 1
        val viewTypeAd = 2
    }

    @SuppressLint("NotifyDataSetChanged")
    fun upData(d: ArrayList<BaseItem>) {
        data.clear()
        data.addAll(d)
        notifyDataSetChanged()
    }

    var clickNewItemCallback: ((News) -> Unit)? = null

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        when (viewType) {
            viewTypeAd -> {
                return AdMRECViewHolder(NewsItemAdMrecBinding.inflate(LayoutInflater.from(parent.context)).root.apply {
                    layoutParams =
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                })
            }

            else -> {
                return NewsViewHolder(NewsItemNewBinding.inflate(LayoutInflater.from(parent.context)).root.apply {
                    layoutParams =
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            CommonUtil.dp2px(328f)
                        )
                })
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NewsViewHolder -> {
                (data[position] as? NewsItem)?.let { item ->
                    holder.bind(item.news)
                    holder.itemView.noDoubleClick {
                        clickNewItemCallback?.invoke(item.news)
                    }
                }
            }

            is AdMRECViewHolder -> {
                (data[position] as? AdItem)?.let {
                    holder.bind(it)
                }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        (holder as? AdMRECViewHolder)?.onViewAttachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        (holder as? AdMRECViewHolder)?.onViewDetachedFromWindow()
    }

    override fun getItemCount(): Int {
        return data.size
    }
}