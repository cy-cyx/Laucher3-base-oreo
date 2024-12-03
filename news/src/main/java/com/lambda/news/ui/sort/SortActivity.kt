package com.lambda.news.ui.sort

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.lambda.common.base.BaseActivity
import com.lambda.common.utils.StatusBarUtil
import com.lambda.common.utils.marginStatusBarHeight
import com.lambda.news.databinding.NewsActivitySortBinding

class SortActivity : BaseActivity<NewsActivitySortBinding>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SortActivity::class.java))
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): NewsActivitySortBinding {
        return NewsActivitySortBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)
        viewBinding.containerLl.marginStatusBarHeight()


    }
}