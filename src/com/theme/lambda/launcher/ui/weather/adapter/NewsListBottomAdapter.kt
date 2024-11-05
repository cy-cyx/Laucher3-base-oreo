package com.lambdaweather.adapter

import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.android.launcher3.R
import com.android.launcher3.databinding.ItemNewsDetailsBottomBinding
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lambdaweather.data.model.NewsModel
import com.lambdaweather.utils.GlideUtil

class NewsListBottomAdapter(val fragment: Fragment) :
    BaseMultiItemQuickAdapter<NewsModel.NewsDTO, BaseViewHolder>(),
    LoadMoreModule {
    init {
        addItemType(0, R.layout.item_news_details_bottom)
    }

    override fun convert(holder: BaseViewHolder, item: NewsModel.NewsDTO) {
        when (item.itemType) {
            else -> {
                val binding = ItemNewsDetailsBottomBinding.bind(holder.itemView)
                item.image_urls?.get(0)?.let { it1 ->
                    GlideUtil.loadUrlImage(
                        fragment,
                        it1, binding.ivIcon, ph = R.drawable.ic_news_ph
                    )
                }
                binding.tvAuthor.text = item.author
                binding.tvTitle.text = HtmlCompat.fromHtml(
                    item.title!!,
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                ).toString()
                binding.tvTime.text = item.publishDate.toString()
            }
        }
    }
}