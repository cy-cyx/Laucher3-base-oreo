package com.theme.lambda.launcher.ui.iconsetting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.android.launcher3.AdjustConfig
import com.android.launcher3.databinding.ActivityIconSettingBinding
import com.theme.lambda.launcher.base.BaseActivity
import com.theme.lambda.launcher.utils.StatusBarUtil
import com.theme.lambda.launcher.utils.marginStatusBarHeight

class IconSettingActivity : BaseActivity<ActivityIconSettingBinding>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, IconSettingActivity::class.java))
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityIconSettingBinding {
        return ActivityIconSettingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)
        viewBinding.containerLl.marginStatusBarHeight()

        viewBinding.backIv.setOnClickListener {
            finish()
        }

        viewBinding.homeScreenIconSizePv.callback = {
            AdjustConfig.setHomeScreenIconSize(AdjustConfig.progressToPercent(it))
        }
        viewBinding.appDrawerIconSizePv.callback = {
            AdjustConfig.setAppDrawerIconSize(AdjustConfig.progressToPercent(it))
        }
        viewBinding.folderSizePv.callback = {
            AdjustConfig.setFolderIconSize(AdjustConfig.progressToPercent(it))
        }

        viewBinding.homeScreenIconSizePv.setProgress(AdjustConfig.percentToProgress(AdjustConfig.getHomeScreenIconSize()))
        viewBinding.appDrawerIconSizePv.setProgress(AdjustConfig.percentToProgress(AdjustConfig.getAppDrawerIconSize()))
        viewBinding.folderSizePv.setProgress(AdjustConfig.percentToProgress(AdjustConfig.getFolderIconSize()))
    }
}