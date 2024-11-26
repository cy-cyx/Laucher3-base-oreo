package com.theme.lambda.launcher.ui.splash

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.android.launcher3.Launcher
import com.android.launcher3.databinding.ActivitySplashBinding
import com.lambda.adlib.LambdaAd
import com.theme.lambda.launcher.Constants
import com.theme.lambda.launcher.ad.AdName
import com.theme.lambda.launcher.ad.AdUtil
import com.theme.lambda.launcher.ad.IAdCallBack
import com.theme.lambda.launcher.base.BaseActivity
import com.theme.lambda.launcher.netstate.NetStateUtil
import com.theme.lambda.launcher.netstate.NetworkType
import com.theme.lambda.launcher.statistics.EventName
import com.theme.lambda.launcher.statistics.EventUtil
import com.theme.lambda.launcher.ui.theme.ThemeActivity
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.LauncherUtil
import com.theme.lambda.launcher.utils.StatusBarUtil
import com.theme.lambda.launcher.utils.visible
import com.theme.lambda.launcher.vip.VipManager
import com.theme.lambda.launcher.widget.dialog.LoadingDialog
import com.theme.lambda.launcher.widget.dialog.NetErrorDialog
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
    private var isResume = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)

        viewBinding.privacyPolicyTv.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        viewBinding.privacyPolicyTv.setOnClickListener {
            CommonUtil.openWebView(this@SplashActivity, Constants.PRIVACY_POLICY)
        }

        viewBinding.termsOfServiceTv.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        viewBinding.termsOfServiceTv.setOnClickListener {
            CommonUtil.openWebView(this@SplashActivity, Constants.TERMS_OF_SERVICE)
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
        // 主要解决升级重装后 拉起入口activity 保证是主屏幕就保证不会再进入闪屏页
        if (LauncherUtil.isDefaultLauncher(this)) {
            startActivity(Intent(this, Launcher::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        isResume = true
        if (!LauncherUtil.isDefaultLauncher(this)) {
            startLoadingCheckNet()
            // 保底ad没有回调返回
            if (isShowAd) {
                gotoNext()
            }
            // 处理重连网络
            if (hasClickConnectNow) {
                hasClickConnectNow = false
                cacheBlock?.let {
                    checkNetState(it)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        isResume = false
    }

    private var isLoading = false

    private fun startLoadingCheckNet() {
        if (isLoading) return
        isLoading = true

        checkNetState() {
            startLoading()
        }
    }

    private fun startLoading() {
        viewBinding.loadingTv.visible()
        viewBinding.progressPv.visible()

        showTryAdTimestamp = System.currentTimeMillis()
        viewBinding.progressPv.startProgress(adWaitingTime) {
            tryToShowAd()
            logLiveEvent()
        }

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
        if (System.currentTimeMillis() - showTryAdTimestamp > adWaitingTime / 2) {
            // 等待超过一半时间 此时都不用等优先级高的广告的加载
            isWantedAdReady = AdUtil.isReady(AdName.splash)
        } else {
            when (region) {
                "RU" -> {
                    val isWantedIntReady = AdUtil.AdPriorityAdSourcesRu.any {
                        AdUtil.isReady(AdName.splash, it, LambdaAd.LambdaAdType.TYPE_INTERSTITIAL)
                    }
                    val isWantedOpenReady = AdUtil.AdPriorityAdSourcesRu.any {
                        AdUtil.isReady(AdName.splash, it, LambdaAd.LambdaAdType.TYPE_OPEN)
                    }
                    // 让int和open充分竞争
                    isWantedAdReady = isWantedIntReady && isWantedOpenReady
                }

                else -> {
                    val isWantedIntReady = AdUtil.AdPriorityAdSources.any {
                        AdUtil.isReady(AdName.splash, it, LambdaAd.LambdaAdType.TYPE_INTERSTITIAL)
                    }
                    val isWantedOpenReady = AdUtil.AdPriorityAdSources.any {
                        AdUtil.isReady(AdName.splash, it, LambdaAd.LambdaAdType.TYPE_OPEN)
                    }
                    // 让int和open充分竞争
                    isWantedAdReady = isWantedIntReady && isWantedOpenReady
                }
            }
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

    private var cacheBlock: (() -> Unit)? = null
    private var hasClickConnectNow = false
    private val loadingDialog by lazy {
        LoadingDialog(this).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

    private fun checkNetState(block: (() -> Unit)) {
        if (NetStateUtil.getNetworkType(this) != NetworkType.NETWORK_NO) {
            block()
            cacheBlock = null
        } else {
            cacheBlock = block
            NetErrorDialog(this).apply {
                clickRetryListen = {
                    dismiss()
                    lifecycleScope.launch {
                        loadingDialog.show()
                        delay(3000)
                        if (!isDestroyed) {
                            loadingDialog.dismiss()

                            cacheBlock?.let {
                                checkNetState(it)
                            }
                        }
                    }
                }
                clickConnectNewListen = {
                    dismiss()
                    NetStateUtil.openNetSettingUI(this@SplashActivity)
                    hasClickConnectNow = true
                }
            }.show()
        }
    }

    private var curLogLiveTime = 0
    private var logEventLast = 0L
    private fun logLiveEvent() {
        if (!isResume) return
        // 500 毫秒尝试统计，避免循环带来无用代码调用
        if (System.currentTimeMillis() - logEventLast < 500) {
            return
        }
        logEventLast = System.currentTimeMillis()

        if (System.currentTimeMillis() - showTryAdTimestamp > 30000 && curLogLiveTime < 30) {
            EventUtil.logEvent(EventName.splashPageView, Bundle().apply {
                putInt("duration_s", 30)
            })
            curLogLiveTime = 30
        } else if (System.currentTimeMillis() - showTryAdTimestamp > 25000 && curLogLiveTime < 25) {
            EventUtil.logEvent(EventName.splashPageView, Bundle().apply {
                putInt("duration_s", 25)
            })
            curLogLiveTime = 25
        } else if (System.currentTimeMillis() - showTryAdTimestamp > 20000 && curLogLiveTime < 20) {
            EventUtil.logEvent(EventName.splashPageView, Bundle().apply {
                putInt("duration_s", 20)
            })
            curLogLiveTime = 20
        } else if (System.currentTimeMillis() - showTryAdTimestamp > 15000 && curLogLiveTime < 15) {
            EventUtil.logEvent(EventName.splashPageView, Bundle().apply {
                putInt("duration_s", 15)
            })
            curLogLiveTime = 15
        } else if (System.currentTimeMillis() - showTryAdTimestamp > 10000 && curLogLiveTime < 10) {
            EventUtil.logEvent(EventName.splashPageView, Bundle().apply {
                putInt("duration_s", 10)
            })
            curLogLiveTime = 10
        } else if (System.currentTimeMillis() - showTryAdTimestamp > 5000 && curLogLiveTime < 5) {
            EventUtil.logEvent(EventName.splashPageView, Bundle().apply {
                putInt("duration_s", 5)
            })
            curLogLiveTime = 5
        }

    }
}