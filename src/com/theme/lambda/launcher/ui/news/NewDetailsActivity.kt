package com.theme.lambda.launcher.ui.news

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.launcher3.Launcher
import com.android.launcher3.databinding.ActivityNewDetailBinding
import com.lambda.common.base.BaseActivity
import com.lambda.common.base.BaseItem
import com.lambda.common.utils.CommonUtil
import com.lambda.common.utils.GsonUtil
import com.lambda.common.utils.StatusBarUtil
import com.lambda.common.utils.marginStatusBarHeight
import com.lambda.remoteconfig.LambdaRemoteConfig
import com.lambda.news.data.model.NewsConfig
import com.lambda.news.data.model.News
import com.theme.lambda.launcher.ui.news.adpater.NewDetailsAdapter
import com.theme.lambda.launcher.ui.news.item.NewDetailsAdItem
import com.theme.lambda.launcher.ui.news.item.NewDetailsItem
import com.theme.lambda.launcher.ui.news.item.NewDetailsTopItem

class NewDetailsActivity : BaseActivity<ActivityNewDetailBinding>() {

    companion object {

        val sKeyNewDetail = "sKeyNewDetail"
        val newsConfig: NewsConfig by lazy {
            val string =
                LambdaRemoteConfig.getInstance(CommonUtil.appContext!!).getString("NewsConfig") ?: ""
            GsonUtil.gson.fromJson(string, NewsConfig::class.java) ?: NewsConfig()
        }

        fun start(context: Context, new: News) {
            val data = GsonUtil.gson.toJson(new)
            context.startActivity(Intent(context, NewDetailsActivity::class.java).apply {
                putExtra(sKeyNewDetail, data)
                if (context is Launcher) {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
            })
        }

        fun start(context: Context, new: String) {
            context.startActivity(Intent(context, NewDetailsActivity::class.java).apply {
                putExtra(sKeyNewDetail, new)
                if (context is Launcher) {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
            })
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityNewDetailBinding {
        return ActivityNewDetailBinding.inflate(layoutInflater)
    }

    private var news: News? = null
    private val newDetailsAdapter: NewDetailsAdapter by lazy { NewDetailsAdapter() }

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

        viewBinding.netIv.setOnClickListener {
            news?.url?.let {
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

        var interval = 1
        news?.let {
            viewBinding.netTv.text = it.url

            val text = it.text.replace("\n", "\n\n")
            val stringList = text.split("\n\n")

            // 处理显示数据
            val data = arrayListOf<BaseItem>()
            data.add(NewDetailsTopItem(it))
            if (newsConfig.enableDetailTopAd) {
                data.add(NewDetailsAdItem())
            }
            for (s in stringList) {
                data.add(NewDetailsItem(s))
                if (interval % newsConfig.detailAdInterval == 0) {
                    data.add(NewDetailsAdItem())
                }
                interval++
            }
            if (data.last() is NewDetailsItem && newsConfig.enableDetailBottomAd) {
                data.add(NewDetailsAdItem())
            }
            newDetailsAdapter.upData(data)
        }
    }
}