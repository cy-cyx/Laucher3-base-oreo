package com.theme.lambda.launcher.ui.theme

import android.app.ProgressDialog
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.databinding.FragmentThemeBinding
import com.theme.lambda.launcher.base.BaseFragment
import com.theme.lambda.launcher.ui.theme.adapter.ThemeAdapter
import com.theme.lambda.launcher.utils.CommonUtil

class ThemeFragment : BaseFragment<FragmentThemeBinding>() {

    var themeTag = ""
    var from = ""

    val viewModel by viewModels<ThemeViewModel>()

    val loadDialog by lazy {
        ProgressDialog(context).apply {
            setTitle("loading...")
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

        viewBinding.swipeRefreshSrl.setOnRefreshListener {
            viewModel.refresh()
        }
        viewBinding.swipeRefreshSrl.setOnLoadMoreListener {
            viewModel.loadMore()
        }

        viewModel.themeLiveData.observe(viewLifecycleOwner, Observer {
            themeAdapter.upData(it)
        })
        viewModel.refreshFinishLiveData.observe(viewLifecycleOwner, Observer {
            viewBinding.swipeRefreshSrl.finishRefresh(500, true, false)
        })
        viewModel.loadMoreFinishLiveData.observe(viewLifecycleOwner, Observer {
            viewBinding.swipeRefreshSrl.finishLoadMore()
        })

        viewModel.refresh()
        viewBinding.swipeRefreshSrl.autoRefresh()

        viewModel.loadDialogLiveData.observe(viewLifecycleOwner, Observer {
            if (it) {
                loadDialog.show()
            } else {
                loadDialog.dismiss()
            }
        })

        themeAdapter.clickItemListen = {
            viewModel.downloadAndGotoPreview(requireActivity(), it)
        }
    }
}