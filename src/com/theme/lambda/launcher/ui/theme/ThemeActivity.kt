package com.theme.lambda.launcher.ui.theme

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import com.android.launcher3.databinding.ActivityThemeBinding
import com.google.android.material.tabs.TabLayout
import com.theme.lambda.launcher.Constants
import com.theme.lambda.launcher.ad.AdName
import com.theme.lambda.launcher.base.BaseActivity
import com.theme.lambda.launcher.statistics.EventName
import com.theme.lambda.launcher.statistics.EventUtil
import com.theme.lambda.launcher.statistics.FirebaseAnalyticsUtil
import com.theme.lambda.launcher.ui.iap.VipActivity
import com.theme.lambda.launcher.ui.me.MeActivity
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
import dalvik.system.ZipPathValidator
import java.lang.ref.WeakReference

class ThemeActivity : BaseActivity<ActivityThemeBinding>() {

    companion object {

        var sFromSplash = "splash"
        var sFromTheme = "theme"

        var sKeyFrom = "key_from"

        var themeActivity: WeakReference<ThemeActivity>? = null

        fun start(context: Context, from: String) {
            context.startActivity(Intent(context, ThemeActivity::class.java).apply {
                putExtra(sKeyFrom, from)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                // 设置成功返回可能会存在异常（可能会回到旧的launcher上），故如此处理
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
        }

        // 预览不能通过栈顶出，故先用此方法
        fun closeThemeActivity() {
            themeActivity?.get()?.finish()
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityThemeBinding {
        return ActivityThemeBinding.inflate(layoutInflater)
    }

    var pageFrom = ThemeActivity.sFromTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themeActivity = WeakReference(this)

        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)
        viewBinding.containerLl.marginStatusBarHeight()

        pageFrom = intent.getStringExtra(sKeyFrom) ?: ThemeActivity.sFromTheme

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
            VipActivity.start(this)
        }
        viewBinding.vipBannerFl.setOnClickListener {
            VipActivity.start(this)
        }

        VipManager.isVip.observe(this, Observer {
            if (it) {
                viewBinding.adBanner.gone()
                viewBinding.vipBannerFl.gone()
            } else {
                viewBinding.vipBannerFl.visible()
            }
        })


        // https://stackoverflow.com/questions/77683434/the-getnextentry-method-of-zipinputstream-throws-a-zipexception-invalid-zip-ent/77697327#77697327
        if (Build.VERSION.SDK_INT >= 34) {
            ZipPathValidator.clearCallback()
        }

        VipManager.bindVipActivity(this)
        EventUtil.logEvent(EventName.homePageView, Bundle())

        requestNotificationPermission()
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

            if (LauncherUtil.gotoSetting) {
                EventUtil.logEvent(EventName.permissionGrant, Bundle().apply {
                    putString("scene", "home")
                    putString("permission", "launcher")
                })
            }
        }
        LauncherUtil.gotoSetting = false
        if (NotificationUtil.notificationsEnabled(this)){
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