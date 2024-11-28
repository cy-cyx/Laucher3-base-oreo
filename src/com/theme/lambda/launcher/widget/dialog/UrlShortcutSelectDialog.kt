package com.theme.lambda.launcher.widget.dialog

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.R
import com.android.launcher3.databinding.DialogUrlShortcutSelectBinding
import com.lambda.common.base.BaseDialog
import com.theme.lambda.launcher.data.model.ShortCut
import com.theme.lambda.launcher.data.model.ShortCuts
import com.theme.lambda.launcher.urlshortcut.UrlShortcutManager
import com.lambda.common.utils.CommonUtil
import com.theme.lambda.launcher.widget.adapter.UrlShortcutAdapter
import com.theme.lambda.launcher.widget.adapter.UrlShortcutTagAdapter

class UrlShortcutSelectDialog(context: Context) :
    BaseDialog(context, R.style.Theme_translucentDialog) {

    val viewBinding = DialogUrlShortcutSelectBinding.inflate(LayoutInflater.from(context))

    private val tagAdapter = UrlShortcutTagAdapter()
    private val shortcutAdapter = UrlShortcutAdapter()

    private var data = ArrayList<ShortCuts>()

    var onDismissListener: ((ArrayList<ShortCut>) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(viewBinding.root)

        val params = window?.attributes
        params?.width = CommonUtil.dp2px(330f)
        params?.height = CommonUtil.dp2px(500f)
        window?.attributes = params

        viewBinding.tagRv.apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
                adapter = tagAdapter
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        super.getItemOffsets(outRect, view, parent, state)
                        outRect.left = 0
                        outRect.right = CommonUtil.dp2px(12f)
                        outRect.top = 0
                        outRect.bottom = 0
                    }
                })
            }
        }
        tagAdapter.clickItemListen = {
            (viewBinding.shortcutRv.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(
                it,
                0
            )
        }

        viewBinding.shortcutRv.apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = shortcutAdapter
        }
        viewBinding.shortcutRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
                layoutManager?.let {
                    val position = it.findFirstVisibleItemPosition()
                    scrollTagRv(position)
                }
            }
        })
        viewBinding.closeIv.setOnClickListener {
            dismiss()
        }

        setOnDismissListener {
            // 关闭即修改
            val result = ArrayList<ShortCut>()
            data.forEach { d ->
                d.shortcuts.forEach { shortcut ->
                    if (shortcut.isSelect) {
                        result.add(shortcut)
                    }
                }
            }
            onDismissListener?.invoke(result)
        }
    }

    // 设置一下当前选中的
    fun setData(curShortCuts: ArrayList<ShortCut>) {
        data = UrlShortcutManager.getShortCuts()
        data.forEach { d ->
            d.shortcuts.forEach { shortcut ->
                val find = curShortCuts.find { it.name == shortcut.name }
                shortcut.isSelect = find != null
            }
        }
        shortcutAdapter.upData(data)
    }

    private var curTagPosition = 0
    fun scrollTagRv(position: Int) {
        if (curTagPosition == position) return
        curTagPosition = position
        (viewBinding.tagRv.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(
            position,
            0
        )
        tagAdapter.setCurSelectTag(position)
    }
}