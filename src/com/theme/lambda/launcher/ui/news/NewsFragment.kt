package com.theme.lambda.launcher.ui.news

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.databinding.FragmentNewsBinding
import com.theme.lambda.launcher.base.BaseFragment
import com.theme.lambda.launcher.ui.news.adpater.NewsAdapter
import com.theme.lambda.launcher.utils.CommonUtil

class NewsFragment : BaseFragment<FragmentNewsBinding>() {

    val viewModel by viewModels<NewsViewModel>()

    override fun initViewBinding(inflater: LayoutInflater): FragmentNewsBinding {
        return FragmentNewsBinding.inflate(inflater)
    }

    private val newsAdapter = NewsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.newsRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.bottom = CommonUtil.dp2px(5f)
                }
            })
            setOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && newsAdapter.lastVisibleItem + 3 > newsAdapter.itemCount) {
                        viewModel.loadMore()
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    //最后一个可见的ITEM
                    newsAdapter.lastVisibleItem = layoutManager!!.findLastVisibleItemPosition()
                }
            })
        }

        viewBinding.swipeRefreshSrl.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.newsLiveData.observe(viewLifecycleOwner, Observer {
            newsAdapter.upData(it)
        })
        viewModel.refreshFinishLiveData.observe(viewLifecycleOwner, Observer {
            viewBinding.swipeRefreshSrl.isRefreshing = false
        })
        viewModel.refresh()
        viewBinding.swipeRefreshSrl.isRefreshing = true
    }
}