package com.theme.lambda.launcher.ui.splash

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.android.launcher3.databinding.ActivitySplashBinding
import com.lambda.adlib.LambdaAd
import com.lambda.common.utils.utilcode.util.LogUtils
import com.theme.lambda.launcher.Constants
import com.theme.lambda.launcher.ad.AdName
import com.theme.lambda.launcher.ad.AdUtil
import com.theme.lambda.launcher.ad.IAdCallBack
import com.theme.lambda.launcher.base.BaseActivity
import com.theme.lambda.launcher.statistics.EventName
import com.theme.lambda.launcher.statistics.EventUtil
import com.theme.lambda.launcher.ui.theme.ThemeActivity
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.ShareUtil
import com.theme.lambda.launcher.utils.StatusBarUtil
import com.theme.lambda.launcher.utils.visible
import com.theme.lambda.launcher.vip.VipManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SplashActivity::class.java).apply {
            })
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    private val adWaitingTime = if (VipManager.isVip.value == true) 3000L else 15000L
    private var showTryAdTimestamp = 0L

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
        EventUtil.logEvent(EventName.splashPageView, Bundle())
    }

    override fun onResume() {
        super.onResume()
        startLoading()
        // 保底ad没有回调返回
        if (isShowAd) {
            gotoNext()
        }
    }

    private var isLoading = false

    private fun startLoading() {
        if (isLoading) return
        isLoading = true

        viewBinding.loadingTv.visible()
        viewBinding.progressPv.visible()

        viewBinding.progressPv.startProgress(adWaitingTime) {
            tryToShowAd()
        }
        showTryAdTimestamp = System.currentTimeMillis()

        lifecycleScope.launch {
            delay(adWaitingTime)
            if (!isShowAd) {
                gotoNext()
            }
        }
    }

    private var isShowAd = false

    private var lastTryToShowAdTimeStamp = System.currentTimeMillis()

    private fun tryToShowAd() {
        if (VipManager.isVip.value == true) return
        if (isShowAd) return

        // 500 尝试去展示广告
        if (System.currentTimeMillis() - lastTryToShowAdTimeStamp < 500) {
            return
        }
        lastTryToShowAdTimeStamp = System.currentTimeMillis()

        var isWantedAdReady: Boolean
        var region = CommonUtil.getRegion()
        if (System.currentTimeMillis() - showTryAdTimestamp > adWaitingTime / 2 || region == "RU") {
            // 等待超过一半时间 此时都不用等admob，meta等加载
            isWantedAdReady = AdUtil.isReady(AdName.splash)
        } else {
            val isWantedIntReady = AdUtil.AdmobAdSources.any {
                AdUtil.isReady(AdName.splash, it, LambdaAd.LambdaAdType.TYPE_INTERSTITIAL)
            }
            val isWantedOpenReady = AdUtil.AdmobAdSources.any {
                AdUtil.isReady(AdName.splash, it, LambdaAd.LambdaAdType.TYPE_OPEN)
            }
            // 让int和open充分竞争
            isWantedAdReady = isWantedIntReady && isWantedOpenReady
        }
        if (isWantedAdReady) {
            isShowAd = true
            AdUtil.showAd(AdName.splash, object : IAdCallBack {
                override fun onNoReady() {
                    isShowAd = false
                }

                override fun onAdClose(status: Int) {
                    gotoNext()
                }
            })
        }

    }

    private var hasGotoNext = false

    private fun gotoNext() {
        if (hasGotoNext) return
        hasGotoNext = true
        ThemeActivity.start(this@SplashActivity, ThemeActivity.sFromSplash)
        finish()
    }
}