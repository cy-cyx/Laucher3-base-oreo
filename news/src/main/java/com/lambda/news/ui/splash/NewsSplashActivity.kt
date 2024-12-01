package com.lambda.news.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.lambda.common.base.BaseActivity
import com.lambda.news.databinding.NewsActivitySplashBinding


class NewsSplashActivity : BaseActivity<NewsActivitySplashBinding>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, NewsSplashActivity::class.java))
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): NewsActivitySplashBinding {
        return NewsActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
}