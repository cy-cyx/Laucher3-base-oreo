package com.theme.lambda.launcher.ui.news.adpater

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.databinding.ItemNewDetailsAdBinding
import com.android.launcher3.databinding.ItemNewDetailsBinding
import com.android.launcher3.databinding.ItemNewDetailsTopBinding
import com.theme.lambda.launcher.base.BaseItem
import com.theme.lambda.launcher.ui.news.item.NewDetailsAdItem
import com.theme.lambda.launcher.ui.news.item.NewDetailsItem
import com.theme.lambda.launcher.ui.news.item.NewDetailsTopItem

class NewDetailsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data = ArrayList<BaseItem>()

    companion object {
        const val viewTypeNewDetails = 0
        const val viewTypeNewDetailsTop = 1
        const val viewTypeNewDetailsAd = 2
    }

    @SuppressLint("NotifyDataSetChanged")
    fun upData(d: ArrayList<BaseItem>) {
        data.clear()
        data.addAll(d)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val baseItem = data[position]
        when (baseItem) {
            is NewDetailsItem -> {
                return viewTypeNewDetails
            }

            is NewDetailsTopItem -> {
                return viewTypeNewDetailsTop
            }

            is NewDetailsAdItem -> {
                return viewTypeNewDetailsAd
            }
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            viewTypeNewDetailsTop -> {
                return NewDetailsTopViewHolder(
                    ItemNewDetailsTopBinding.inflate(
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
                    ItemNewDetailsAdBinding.inflate(
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

            else -> {
                return NewDetailsViewHolder(
                    ItemNewDetailsBinding.inflate(
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
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}