package com.theme.lambda.launcher.ui.splash

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.lifecycleScope
import com.android.launcher3.databinding.ActivitySplashBinding
import com.theme.lambda.launcher.Constants
import com.theme.lambda.launcher.base.BaseActivity
import com.theme.lambda.launcher.ui.theme.ThemeActivity
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.ShareUtil
import com.theme.lambda.launcher.utils.StatusBarUtil
import com.theme.lambda.launcher.utils.gone
import com.theme.lambda.launcher.utils.marginStatusBarHeight
import com.theme.lambda.launcher.utils.visible
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        window.decorView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )
    }

    override fun onResume() {
        super.onResume()
        startLoading()
    }

    private var isLoading = false

    private fun startLoading() {
        if (isLoading) return
        isLoading = true

        viewBinding.loadingTv.visible()
        viewBinding.progressPv.visible()

        viewBinding.progressPv.startProgress(5000) {

        }

        lifecycleScope.launch {
            delay(5000)
            ThemeActivity.start(this@SplashActivity, ThemeActivity.sFromSplash)
            finish()
        }
    }
}