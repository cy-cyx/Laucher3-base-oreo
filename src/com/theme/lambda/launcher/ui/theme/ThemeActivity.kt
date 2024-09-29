package com.theme.lambda.launcher.ui.theme

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import com.android.launcher3.databinding.ActivityThemeBinding
import com.theme.lambda.launcher.Constants
import com.theme.lambda.launcher.base.BaseActivity
import com.theme.lambda.launcher.ui.me.MeActivity
import com.theme.lambda.launcher.utils.LauncherUtil
import com.theme.lambda.launcher.utils.StatusBarUtil
import com.theme.lambda.launcher.utils.marginStatusBarHeight
import com.theme.lambda.launcher.utils.visible
import com.theme.lambda.launcher.widget.adapter.LauncherFragmentAdapter
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

        viewBinding.meThemeIv.setOnClickListener {
            MeActivity.start(this)
        }

        if (!LauncherUtil.isDefaultLauncher(this)) {
            viewBinding.applyTv.visible()
            viewBinding.applyTv.setOnClickListener {
                LauncherUtil.gotoSetLauncher(this)
            }
        }

        // https://stackoverflow.com/questions/77683434/the-getnextentry-method-of-zipinputstream-throws-a-zipexception-invalid-zip-ent/77697327#77697327
        if (Build.VERSION.SDK_INT >= 34) {
            ZipPathValidator.clearCallback()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        themeActivity = null
    }
}