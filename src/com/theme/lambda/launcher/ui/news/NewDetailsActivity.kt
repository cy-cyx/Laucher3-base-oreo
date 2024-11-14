package com.theme.lambda.launcher.ui.news

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.launcher3.Launcher
import com.android.launcher3.databinding.ActivityNewDetailBinding
import com.theme.lambda.launcher.base.BaseActivity
import com.theme.lambda.launcher.base.BaseItem
import com.theme.lambda.launcher.data.model.News
import com.theme.lambda.launcher.ui.news.adpater.NewDetailsAdapter
import com.theme.lambda.launcher.ui.news.item.NewDetailsAdItem
import com.theme.lambda.launcher.ui.news.item.NewDetailsItem
import com.theme.lambda.launcher.ui.news.item.NewDetailsTopItem
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.GsonUtil
import com.theme.lambda.launcher.utils.StatusBarUtil
import com.theme.lambda.launcher.utils.marginStatusBarHeight

class NewDetailsActivity : BaseActivity<ActivityNewDetailBinding>() {

    companion object {

        val sKeyNewDetail = "sKeyNewDetail"

        fun start(context: Context, new: News) {
            val data = GsonUtil.gson.toJson(new)
            context.startActivity(Intent(context, NewDetailsActivity::class.java).apply {
                putExtra(sKeyNewDetail, data)
                if (context is Launcher) {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
            })
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityNewDetailBinding {
        return ActivityNewDetailBinding.inflate(layoutInflater)
    }

    private var new: News? = null
    private val newDetailsAdapter: NewDetailsAdapter by lazy { NewDetailsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)
        viewBinding.containerLl.marginStatusBarHeight()

        intent.getStringExtra(sKeyNewDetail)?.let {
            if (it.isNotBlank()) {
                try {
                    new = GsonUtil.gson.fromJson(it, News::class.java)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        viewBinding.netIv.setOnClickListener {
            new?.url?.let {
                CommonUtil.openWebView(this, it)
            }
        }

        viewBinding.backIv.setOnClickListener { finish() }

        viewBinding.newRv.apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = newDetailsAdapter
        }

        // 3个换行塞一个广告
        var interval = 3

        new?.let {
            viewBinding.netTv.text = it.url

            val text = it.text.replace("\n", "\n\n")
            val stringList = text.split("\n\n")

            // 处理显示数据
            val data = arrayListOf<BaseItem>()
            data.add(NewDetailsTopItem(it))
            for (s in stringList) {
                data.add(NewDetailsItem(s))
                if (interval % 3 == 0) {
                    data.add(NewDetailsAdItem())
                }
                interval++
            }
            if (data.last() is NewDetailsItem) {
                data.add(NewDetailsAdItem())
            }
            newDetailsAdapter.upData(data)
        }
    }
}