package com.theme.lambda.launcher.ui.theme

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustEvent
import com.android.launcher3.BuildConfig
import com.android.launcher3.RecommendAppManager
import com.android.launcher3.databinding.ActivityThemeBinding
import com.google.android.material.tabs.TabLayout
import com.lambda.remoteconfig.LambdaRemoteConfig
import com.theme.lambda.launcher.Constants
import com.theme.lambda.launcher.ad.AdName
import com.theme.lambda.launcher.ad.AdUtil
import com.theme.lambda.launcher.base.BaseActivity
import com.theme.lambda.launcher.statistics.EventName
import com.theme.lambda.launcher.statistics.EventUtil
import com.theme.lambda.launcher.statistics.FirebaseAnalyticsUtil
import com.theme.lambda.launcher.ui.iap.VipActivity
import com.theme.lambda.launcher.ui.me.MeActivity
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.LauncherUtil
import com.theme.lambda.launcher.utils.NotificationUtil
import com.theme.lambda.launcher.utils.PermissionUtil
import com.theme.lambda.launcher.utils.StatusBarUtil
import com.theme.lambda.launcher.utils.gone
import com.theme.lambda.launcher.utils.marginStatusBarHeight
import com.theme.lambda.launcher.utils.visible
import com.theme.lambda.launcher.vip.VipManager
import com.theme.lambda.launcher.widget.adapter.LauncherFragmentAdapter
import com.theme.lambda.launcher.widget.dialog.ApplyLauncherPermissionDialog
import com.theme.lambda.launcher.widget.dialog.SetDefaultFailedDialog
import dalvik.system.ZipPathValidator
import java.lang.ref.WeakReference

class ThemeActivity : BaseActivity<ActivityThemeBinding>() {

    companion object {
        @JvmField
        var sFromSplash = "splash"

        @JvmField
        var sFromTheme = "theme"

        var sKeyFrom = "key_from"

        var themeActivity: WeakReference<ThemeActivity>? = null

        @JvmStatic
        fun start(context: Context, from: String) {
            context.startActivity(Intent(context, ThemeActivity::class.java).apply {
                putExtra(sKeyFrom, from)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                if (LauncherUtil.isDefaultLauncher(context)) {
                    addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                }
                // 设置成功返回可能会存在异常（可能会回到旧的launcher上），故如此处理
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
            // 测试模式下，点击主题刷新一下配置
            if (BuildConfig.isDebug && from == sFromTheme) {
                LambdaRemoteConfig.getInstance(CommonUtil.appContext!!).fetchAndActivate(Constants.configKeys)
            }
        }

        // 预览不能通过栈顶出，故先用此方法
        fun closeThemeActivity() {
            themeActivity?.get()?.finish()
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityThemeBinding {
        return ActivityThemeBinding.inflate(layoutInflater)
    }

    var pageFrom = sFromTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themeActivity = WeakReference(this)

        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)
        viewBinding.containerLl.marginStatusBarHeight()

        pageFrom = intent.getStringExtra(sKeyFrom) ?: sFromTheme

        viewBinding.tabTl.apply {
            Constants.sThemeTag.forEach {
                addTab(viewBinding.tabTl.newTab())
            }
        }

        viewBinding.themeVp.apply {

            adapter = LauncherFragmentAdapter(supportFragmentManager).apply {

                // 多种主题
                Constants.sThemeTag.forEach {
                    fragments.add(ThemeFragment().apply {
                        themeTag = it
                        from = pageFrom
                    })

                    fragmentsTitle.add(it)
                }
            }
        }
        viewBinding.tabTl.setupWithViewPager(viewBinding.themeVp)
        viewBinding.tabTl.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (!EventUtil.hasLogHomeChangeTag) {
                    EventUtil.logEvent(EventName.homePageInteract, Bundle().apply {
                        putString("type", "change_tag")
                    })
                    EventUtil.hasLogHomeChangeTag = true
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        viewBinding.meThemeIv.setOnClickListener {
            MeActivity.start(this)
            EventUtil.logEvent(EventName.homePageInteract, Bundle().apply {
                putString("type", "mine")
            })
        }

        if (!LauncherUtil.isDefaultLauncher(this)) {
            viewBinding.applyTv.visible()
            viewBinding.applyTv.setOnClickListener {
                ApplyLauncherPermissionDialog(this).apply {
                    from = ApplyLauncherPermissionDialog.sFromHome
                    clickApplyListen = {
                        dismiss()
                        LauncherUtil.gotoSetLauncher(this@ThemeActivity)
                        EventUtil.logEvent(EventName.permissionDialog2Show, Bundle().apply {
                            putString("scene", ApplyLauncherPermissionDialog.sFromHome)
                            putString("permission", "launcher")
                        })
                    }
                    clickNotNowListen = {
                        dismiss()
                    }
                }.show()
                EventUtil.logEvent(EventName.homePageInteract, Bundle().apply {
                    putString("type", "apply_theme")
                })
            }
        }

        viewBinding.adBanner.scenesName = AdName.home_ban

        viewBinding.vipIv.setOnClickListener {
            VipActivity.start(this, VipActivity.FromHomeIcon)
        }
        viewBinding.vipBannerFl.setOnClickListener {
            VipActivity.start(this, VipActivity.FromHomeBanner)
        }

        VipManager.isVip.observe(this, Observer {
            if (it) {
                viewBinding.adBanner.gone()
                viewBinding.vipBannerFl.gone()
                viewBinding.vipIv.gone()
            } else {
                viewBinding.vipBannerFl.visible()
                viewBinding.vipIv.visible()
                EventUtil.logEvent(EventName.iapEntryView, Bundle().apply {
                    putString("placement", VipActivity.FromHomeIcon)
                })
                EventUtil.logEvent(EventName.iapEntryView, Bundle().apply {
                    putString("placement", VipActivity.FromHomeBanner)
                })
            }
        })

        // https://stackoverflow.com/questions/77683434/the-getnextentry-method-of-zipinputstream-throws-a-zipexception-invalid-zip-ent/77697327#77697327
        if (Build.VERSION.SDK_INT >= 34) {
            ZipPathValidator.clearCallback()
        }

        VipManager.bindVipActivity(this)
        EventUtil.logEvent(EventName.homePageView, Bundle())

        requestNotificationPermission()

        // 进入首页加载所有广告
        AdUtil.loadAd(this, false)

        RecommendAppManager.init(this)
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionUtil.requestRuntimePermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                object : PermissionUtil.IPermissionCallback {
                    override fun nextStep() {
                        EventUtil.logEvent(EventName.permissionGrant, Bundle().apply {
                            putString("scene", "home")
                            putString("permission", "notification")
                        })
                    }

                    override fun noPermission() {

                    }

                    override fun gotoSet(internal: Boolean) {

                    }
                },
                force = false,
                showGotoSetDialog = false
            )
        }
    }

    override fun onResume() {
        super.onResume()
        // 重建回来
        if (LauncherUtil.isDefaultLauncher(this)) {
            viewBinding.applyTv.gone()
            EventUtil.logEvent(EventName.activate, Bundle())
            FirebaseAnalyticsUtil.logEvent(EventName.activate, Bundle())
            Adjust.trackEvent(AdjustEvent("wbnbw3"))
        }
        if (LauncherUtil.gotoSetting) {
            if (LauncherUtil.isDefaultLauncher(this)) {
                EventUtil.logEvent(EventName.permissionGrant, Bundle().apply {
                    putString("scene", "home")
                    putString("permission", "launcher")
                })
            } else {
                // 设置回来失败
                SetDefaultFailedDialog(this).show()
            }
        }
        LauncherUtil.gotoSetting = false
        if (NotificationUtil.notificationsEnabled(this)) {
            EventUtil.logEvent(EventName.permissionGrant, Bundle().apply {
                putString("scene", "home")
                putString("permission", "notification")
            })
        }

        VipManager.upDataFreeAdUntil()
    }

    override fun onDestroy() {
        super.onDestroy()
        themeActivity = null
    }
}