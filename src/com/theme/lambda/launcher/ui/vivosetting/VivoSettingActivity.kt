package com.theme.lambda.launcher.ui.vivosetting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.android.launcher3.databinding.ActivityVivoSettingBinding
import com.lambda.common.base.BaseActivity
import com.lambda.common.utils.marginStatusBarHeight
import com.theme.lambda.launcher.utils.LauncherUtil
import com.lambda.common.utils.StatusBarUtil

class VivoSettingActivity : BaseActivity<ActivityVivoSettingBinding>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, VivoSettingActivity::class.java))
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityVivoSettingBinding {
        return ActivityVivoSettingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)
        viewBinding.containerLl.marginStatusBarHeight()

        viewBinding.allowFl.setOnClickListener {
            LauncherUtil.gotoSetLauncherWithOutGuide(this)
        }

        viewBinding.backIv.setOnClickListener {
            finish()
        }
    }
}