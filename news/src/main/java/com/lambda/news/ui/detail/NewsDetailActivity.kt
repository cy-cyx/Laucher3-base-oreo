package com.lambda.news.ui.detail

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lambda.common.base.BaseActivity
import com.lambda.common.base.BaseItem
import com.lambda.common.utils.CommonUtil
import com.lambda.common.utils.GsonUtil
import com.lambda.common.utils.StatusBarUtil
import com.lambda.common.utils.marginStatusBarHeight
import com.lambda.news.data.model.News
import com.lambda.news.databinding.NewsActivityDetailBinding
import com.lambda.news.ui.detail.adapter.NewDetailsAdapter
import com.lambda.news.ui.detail.item.NewDetailsAdItem
import com.lambda.news.ui.detail.item.NewDetailsItem
import com.lambda.news.ui.detail.item.NewDetailsTopItem
import com.lambda.news.ui.newslist.item.NewsItem

class NewsDetailActivity : BaseActivity<NewsActivityDetailBinding>() {

    companion object {
        val sKeyNewDetail = "sKeyNewDetail"

        fun start(context: Context, new: News) {
            val data = GsonUtil.gson.toJson(new)
            context.startActivity(Intent(context, NewsDetailActivity::class.java).apply {
                putExtra(sKeyNewDetail, data)
            })
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): NewsActivityDetailBinding {
        return NewsActivityDetailBinding.inflate(layoutInflater)
    }

    private var news: News? = null
    private val newDetailsAdapter: NewDetailsAdapter by lazy { NewDetailsAdapter() }

    private val viewModel by viewModels<NewsDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)
        viewBinding.containerLl.marginStatusBarHeight()

        intent.getStringExtra(sKeyNewDetail)?.let {
            if (it.isNotBlank()) {
                try {
                    news = GsonUtil.gson.fromJson(it, News::class.java)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        viewBinding.backIv.setOnClickListener { finish() }

        viewBinding.newRv.apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = newDetailsAdapter
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    val position =
                        (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
                    if (newDetailsAdapter.data[position] is NewsItem) {
                        outRect.top = CommonUtil.dp2px(4f)
                    }
                }
            })
        }

        var interval = 1
        viewModel.new = news
        news?.let {

            val text = it.text.replace("\n", "\n\n")
            val stringList = text.split("\n\n")

            // 处理显示数据
            val data = arrayListOf<BaseItem>()
            data.add(NewDetailsTopItem(it))
            for (s in stringList) {
                data.add(NewDetailsItem(s))
                if (interval % 10 == 0) {
                    data.add(NewDetailsAdItem())
                }
                interval++
            }
            // 最后是文字几个换行
            if (data.last() is NewDetailsItem) {
                (data.last() as NewDetailsItem).text += "\n\n"
            }
            newDetailsAdapter.upData(data)
        }

        newDetailsAdapter.clickNewItemCallback = {
            start(this, it)
        }

        viewModel.moreNewsLiveData.observe(this, Observer {
            newDetailsAdapter.addMoreData(it.map { NewsItem(it) }.toMutableList())
        })
        viewModel.loadMoreNews()
    }
}