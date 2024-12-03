package com.lambda.news.ui.detail.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lambda.common.base.BaseItem
import com.lambda.common.utils.CommonUtil
import com.lambda.common.utils.noDoubleClick
import com.lambda.news.data.model.News
import com.lambda.news.databinding.NewsItemNewBinding
import com.lambda.news.databinding.NewsItemNewDetailsAdBinding
import com.lambda.news.databinding.NewsItemNewDetailsBinding
import com.lambda.news.databinding.NewsItemNewDetailsTopBinding
import com.lambda.news.ui.detail.item.NewDetailsAdItem
import com.lambda.news.ui.detail.item.NewDetailsItem
import com.lambda.news.ui.detail.item.NewDetailsTopItem
import com.lambda.news.ui.newslist.adapter.NewsViewHolder
import com.lambda.news.ui.newslist.item.NewsItem

class NewDetailsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val data = ArrayList<BaseItem>()

    companion object {
        const val viewTypeNewDetails = 0
        const val viewTypeNewDetailsTop = 1
        const val viewTypeNewDetailsAd = 2
        const val viewTypeNews = 3
    }

    var clickNewItemCallback: ((News) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun upData(d: ArrayList<BaseItem>) {
        data.clear()
        data.addAll(d)
        notifyDataSetChanged()
    }

    fun addMoreData(d: MutableList<NewsItem>) {
        data.addAll(d)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        when (data[position]) {
            is NewDetailsItem -> {
                return viewTypeNewDetails
            }

            is NewDetailsTopItem -> {
                return viewTypeNewDetailsTop
            }

            is NewDetailsAdItem -> {
                return viewTypeNewDetailsAd
            }

            is NewsItem -> {
                return viewTypeNews
            }
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            viewTypeNewDetailsTop -> {
                return NewDetailsTopViewHolder(
                    NewsItemNewDetailsTopBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        )
                    ).root.apply {
                        layoutParams =
                            ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                    })
            }

            viewTypeNewDetailsAd -> {
                return NewDetailsAdViewHolder(
                    NewsItemNewDetailsAdBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        )
                    ).root.apply {
                        layoutParams =
                            ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                    })
            }

            viewTypeNews ->{
                return NewsViewHolder(NewsItemNewBinding.inflate(LayoutInflater.from(parent.context)).root.apply {
                    layoutParams =
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            CommonUtil.dp2px(328f)
                        )
                })
            }

            else -> {
                return NewDetailsViewHolder(
                    NewsItemNewDetailsBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        )
                    ).root.apply {
                        layoutParams =
                            ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                    })
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NewDetailsTopViewHolder -> {
                (data[position] as? NewDetailsTopItem)?.let {
                    holder.bind(it.news)
                }
            }

            is NewDetailsAdViewHolder -> {
                holder.bind(data[position] as NewDetailsAdItem)
            }

            is NewDetailsViewHolder -> {
                (data[position] as? NewDetailsItem)?.let {
                    holder.bind(it.text)
                }
            }
            is NewsViewHolder -> {
                (data[position] as? NewsItem)?.let { item ->
                    holder.bind(item.news)
                    holder.itemView.noDoubleClick {
                        clickNewItemCallback?.invoke(item.news)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}