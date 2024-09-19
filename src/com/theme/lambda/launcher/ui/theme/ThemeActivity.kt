package com.theme.lambda.launcher.ui.theme

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.android.launcher3.databinding.ActivityThemeBinding
import com.theme.lambda.launcher.Constants
import com.theme.lambda.launcher.base.BaseActivity
import com.theme.lambda.launcher.widget.adapter.LauncherFragmentAdapter

class ThemeActivity : BaseActivity<ActivityThemeBinding>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, ThemeActivity::class.java))
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityThemeBinding {
        return ActivityThemeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                    })

                    fragmentsTitle.add(it)
                }
            }
        }
        viewBinding.tabTl.setupWithViewPager(viewBinding.themeVp)
    }
}