package com.theme.lambda.launcher.ui.theme

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import com.android.launcher3.databinding.ActivityThemeBinding
import com.theme.lambda.launcher.base.BaseActivity

class ThemeActivity : BaseActivity<ActivityThemeBinding>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, ThemeActivity::class.java))
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityThemeBinding {
        return ActivityThemeBinding.inflate(layoutInflater)
    }
}