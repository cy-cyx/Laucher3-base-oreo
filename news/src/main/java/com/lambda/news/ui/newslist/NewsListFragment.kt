package com.lambda.news.ui.newslist

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lambda.common.base.BaseFragment
import com.lambda.common.utils.CommonUtil
import com.lambda.common.utils.gone
import com.lambda.common.utils.visible
import com.lambda.news.databinding.NewsFragmentNewsListBinding
import com.lambda.news.ui.detail.NewsDetailActivity
import com.lambda.news.ui.newslist.adapter.NewListAdapter

class NewsListFragment : BaseFragment<NewsFragmentNewsListBinding>() {
    override fun initViewBinding(inflater: LayoutInflater): NewsFragmentNewsListBinding {
        return NewsFragmentNewsListBinding.inflate(inflater)
    }

    var category = ""

    private var viewModel = NewsListViewModel()
    private var newsAdapter = NewListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.category = category

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
                    outRect.top = CommonUtil.dp2px(4f)
                }
            })
        }

        newsAdapter.clickNewItemCallback = {
            NewsDetailActivity.start(requireContext(), it, NewsDetailActivity.sFromHome)
        }

        viewBinding.swipeRefreshSrl.setOnRefreshListener {
            viewModel.refresh()
        }
        viewBinding.swipeRefreshSrl.setOnLoadMoreListener {
            viewModel.loadMore()
        }

        viewModel.newsLiveData.observe(viewLifecycleOwner, Observer {
            newsAdapter.upData(it)

            if (it.isEmpty()) {
                viewBinding.emptyFl.visible()
            } else {
                viewBinding.emptyFl.gone()
            }
        })
        viewModel.refreshFinishLiveData.observe(viewLifecycleOwner, Observer {
            viewBinding.swipeRefreshSrl.finishRefresh(500, true, false)
        })
        viewModel.loadMoreFinishLiveData.observe(viewLifecycleOwner, Observer {
            viewBinding.swipeRefreshSrl.finishLoadMore()
        })

        viewModel.refresh()
        viewBinding.swipeRefreshSrl.autoRefresh()
    }
}