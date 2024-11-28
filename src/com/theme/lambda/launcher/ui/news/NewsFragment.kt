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
import com.theme.lambda.launcher.utils.gone
import com.theme.lambda.launcher.utils.visible

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
                    outRect.top = CommonUtil.dp2px(5f)
                }
            })
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