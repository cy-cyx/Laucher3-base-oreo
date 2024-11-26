package com.theme.lambda.launcher.ui.layoutadjust

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.android.launcher3.databinding.ActivityLayoutAdjustBinding
import com.theme.lambda.launcher.base.BaseActivity
import com.theme.lambda.launcher.utils.StatusBarUtil
import com.theme.lambda.launcher.utils.marginStatusBarHeight

class LayoutAdjustActivity : BaseActivity<ActivityLayoutAdjustBinding>() {

    companion object {

        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, LayoutAdjustActivity::class.java))
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityLayoutAdjustBinding {
        return ActivityLayoutAdjustBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)
        viewBinding.containerLl.marginStatusBarHeight()

        viewBinding.backIv.setOnClickListener {
            finish()
        }


    }
}