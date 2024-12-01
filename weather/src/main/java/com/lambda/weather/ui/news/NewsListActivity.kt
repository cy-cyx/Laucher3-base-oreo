package com.lambdaweather.ui.news

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.lambda.common.base.BaseActivity
import com.lambda.common.utils.marginStatusBarHeight
import com.lambda.common.utils.StatusBarUtil
import com.lambda.weather.LambdaWeather
import com.lambda.weather.R
import com.lambda.weather.databinding.ActivityNewListBinding

class NewsListActivity : BaseActivity<ActivityNewListBinding>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, NewsListActivity::class.java))
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityNewListBinding {
        return ActivityNewListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)
        viewBinding.containerLl.marginStatusBarHeight()
        supportFragmentManager.beginTransaction().apply {
            LambdaWeather.getNewFragment()?.let {
                replace(R.id.fl, it, "NewsListFragment")
            }
        }.commitNow()

        viewBinding.backIv.setOnClickListener {
            finish()
        }
    }
}