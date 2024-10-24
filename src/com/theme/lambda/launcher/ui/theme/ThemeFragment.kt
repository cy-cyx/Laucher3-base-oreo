package com.theme.lambda.launcher.ui.theme

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import com.android.launcher3.databinding.FragmentThemeBinding
import com.theme.lambda.launcher.base.BaseFragment
import com.theme.lambda.launcher.statistics.EventName
import com.theme.lambda.launcher.statistics.EventUtil
import com.theme.lambda.launcher.ui.theme.adapter.ThemeAdapter
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.gone
import com.theme.lambda.launcher.utils.visible
import com.theme.lambda.launcher.widget.dialog.LoadingDialog

class ThemeFragment : BaseFragment<FragmentThemeBinding>() {

    var themeTag = ""
    var from = ""

    val viewModel by viewModels<ThemeViewModel>()

    val loadDialog by lazy {
        LoadingDialog(requireContext()).apply {
            setCancelable(false)
        }
    }

    private val themeAdapter = ThemeAdapter()

    override fun initViewBinding(inflater: LayoutInflater): FragmentThemeBinding {
        return FragmentThemeBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.tag = themeTag
        viewModel.from = from

        viewBinding.themeRv.apply {
            layoutManager = GridLayoutManager(context, 2).apply {
                orientation = GridLayoutManager.VERTICAL
            }
            adapter = themeAdapter
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.top = CommonUtil.dp2px(12f)
                    outRect.left = CommonUtil.dp2px(6f)
                    outRect.right = CommonUtil.dp2px(6f)
                }
            })
        }
        viewBinding.themeRv.addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_DRAGGING) {
                    if (!EventUtil.hasLogHomeScroll) {
                        EventUtil.logEvent(EventName.homePageInteract, Bundle().apply {
                            putString("type", "scroll")
                        })
                        EventUtil.hasLogHomeScroll = true
                    }
                }
            }
        })

        viewBinding.swipeRefreshSrl.setOnRefreshListener {
            viewModel.refresh()
        }
        viewBinding.swipeRefreshSrl.setOnLoadMoreListener {
            viewModel.loadMore()
        }

        viewModel.themeLiveData.observe(viewLifecycleOwner, Observer {
            themeAdapter.upData(it)

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
        viewModel.refreshCache()
        viewBinding.swipeRefreshSrl.autoRefresh()

        viewModel.loadDialogLiveData.observe(viewLifecycleOwner, Observer {
            if (it) {
                loadDialog.show()
            } else {
                loadDialog.dismiss()
            }
        })

        themeAdapter.clickItemListen = {
            viewModel.gotoPreview(requireActivity(), it)
            EventUtil.logEvent(EventName.homePageInteract, Bundle().apply {
                putString("type", "click_item")
            })
            EventUtil.logEvent(EventName.AppResourcePageClick, Bundle().apply {
                putString("id", it.id)
                putString("cat", "theme")
                putString("tag", it.tag)
            })
        }
    }


}