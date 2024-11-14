package com.theme.lambda.launcher.ui.news

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.android.launcher3.Launcher
import com.android.launcher3.R
import com.android.launcher3.databinding.ActivityNewDetailBinding
import com.theme.lambda.launcher.base.BaseActivity
import com.theme.lambda.launcher.data.model.News
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.GlideUtil
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

        new?.let {
            viewBinding.authorTv.text = it.author
            viewBinding.netTv.text = it.url
            viewBinding.titleTv.text = it.title
            viewBinding.timeTv.text = it.publishDate
            it.image.getOrNull(0)?.let {
                GlideUtil.load(
                    viewBinding.logoIv,
                    it,
                    placeholder = R.drawable.ic_news_ph
                )
            }
            viewBinding.contentTv.text = it.text.replace("\n", "\n\n")
        }


    }
}