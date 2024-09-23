package com.theme.lambda.launcher.ui.me

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.databinding.ActivityMeBinding
import com.theme.lambda.launcher.base.BaseActivity
import com.theme.lambda.launcher.ui.me.adapter.MeThemeAdapter
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.StatusBarUtil
import com.theme.lambda.launcher.utils.marginStatusBarHeight

class MeActivity : BaseActivity<ActivityMeBinding>() {

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, MeActivity::class.java))
        }
    }

    val viewModel by viewModels<MeViewModel>()
    private val themeAdapter = MeThemeAdapter()

    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityMeBinding {
        return ActivityMeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)
        viewBinding.containerLl.marginStatusBarHeight()


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

        viewBinding.backIv.setOnClickListener {
            finish()
        }

        viewBinding.swipeRefreshSrl.setOnRefreshListener {
            viewModel.load()
        }
        themeAdapter.clickItemListen = {
            viewModel.download(this@MeActivity, it)
        }

        viewModel.refreshFinishLiveData.observe(this, Observer {
            viewBinding.swipeRefreshSrl.finishRefresh()
        })

        viewModel.dataLiveDate.observe(this, Observer {
            themeAdapter.upData(it)
        })

        viewModel.load()
        viewBinding.swipeRefreshSrl.autoRefresh()
    }
}