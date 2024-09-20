package com.theme.lambda.launcher.ui.theme

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.android.launcher3.databinding.ActivityThemeBinding
import com.theme.lambda.launcher.Constants
import com.theme.lambda.launcher.base.BaseActivity
import com.theme.lambda.launcher.utils.StatusBarUtil
import com.theme.lambda.launcher.utils.marginStatusBarHeight
import com.theme.lambda.launcher.widget.adapter.LauncherFragmentAdapter

class ThemeActivity : BaseActivity<ActivityThemeBinding>() {

    companion object {

        var sFromSplash = "splash"
        var sFromTheme = "theme"

        var sKeyFrom = "key_from"

        fun start(context: Context, from: String) {
            context.startActivity(Intent(context, ThemeActivity::class.java).apply {
                putExtra(sKeyFrom, from)
            })
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityThemeBinding {
        return ActivityThemeBinding.inflate(layoutInflater)
    }

    var pageFrom = ThemeActivity.sFromTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                        from =pageFrom
                    })

                    fragmentsTitle.add(it)
                }
            }
        }
        viewBinding.tabTl.setupWithViewPager(viewBinding.themeVp)
    }
}