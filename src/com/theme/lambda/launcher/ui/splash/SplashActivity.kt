package com.theme.lambda.launcher.ui.splash

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import com.android.launcher3.databinding.ActivitySplashBinding
import com.theme.lambda.launcher.Constants
import com.theme.lambda.launcher.base.BaseActivity
import com.theme.lambda.launcher.ui.theme.ThemeActivity
import com.theme.lambda.launcher.utils.ShareUtil
import com.theme.lambda.launcher.utils.StatusBarUtil
import com.theme.lambda.launcher.utils.gone
import com.theme.lambda.launcher.utils.visible

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun initViewBinding(layoutInflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)

        viewBinding.privacyPolicyTv.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        viewBinding.privacyPolicyTv.setOnClickListener {
            ShareUtil.openWebView(this@SplashActivity, Constants.PRIVACY_POLICY)
        }

        viewBinding.termsOfServiceTv.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        viewBinding.termsOfServiceTv.setOnClickListener {
            ShareUtil.openWebView(this@SplashActivity, Constants.SECRET_KEY)
        }

        viewBinding.startBnLav.setOnClickListener {
            startLoading()
        }
    }

    private var isLoading = false

    private fun startLoading() {
        if (isLoading) return
        isLoading = true

        viewBinding.startBnLav.gone()
        viewBinding.loadingTv.visible()
        viewBinding.progressPv.visible()

        viewBinding.progressPv.startProgress(10000) {
            ThemeActivity.start(this, ThemeActivity.sFromSplash)
            finish()
        }
    }
}