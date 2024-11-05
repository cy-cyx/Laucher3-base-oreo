package com.lambdaweather.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import com.android.launcher3.R
import com.android.launcher3.databinding.ActivityNewListBinding
import com.theme.lambda.launcher.base.BaseActivity
import com.theme.lambda.launcher.ui.news.NewsFragment
import com.theme.lambda.launcher.utils.StatusBarUtil
import com.theme.lambda.launcher.utils.marginStatusBarHeight

class NewsListActivity : BaseActivity<ActivityNewListBinding>() {
    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityNewListBinding {
        return ActivityNewListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)
        viewBinding.containerLl.marginStatusBarHeight()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl, NewsFragment(), "NewsListFragment")
        }.commitNow()

        viewBinding.backIv.setOnClickListener {
            finish()
        }
    }
}