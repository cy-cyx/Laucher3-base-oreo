package com.theme.lambda.launcher.ui.themepreview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.android.launcher3.databinding.ActivityThemePreviewBinding
import com.lambda.adlib.LambdaAd
import com.lambda.common.ad.AdName
import com.lambda.common.ad.AdUtil
import com.lambda.common.ad.IAdCallBack
import com.lambda.common.base.BaseActivity
import com.theme.lambda.launcher.data.model.Resources
import com.lambda.common.statistics.EventName
import com.lambda.common.statistics.EventUtil.logEvent
import com.theme.lambda.launcher.ui.iap.VipActivity
import com.lambda.common.utils.GlideUtil
import com.lambda.common.utils.GsonUtil
import com.lambda.common.utils.StatusBarUtil
import com.lambda.common.utils.noDoubleClick
import com.theme.lambda.launcher.utils.withHost
import com.lambda.common.vip.VipManager
import com.theme.lambda.launcher.widget.dialog.LoadingWithAdDialog

class ThemePreviewActivity : BaseActivity<ActivityThemePreviewBinding>() {

    companion object {

        val sKeyResource = "key_resource"

        fun start(context: Context, resources: Resources) {
            context.startActivity(Intent(context, ThemePreviewActivity::class.java).apply {
                putExtra(sKeyResource, GsonUtil.gson.toJson(resources))
            })
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityThemePreviewBinding {
        return ActivityThemePreviewBinding.inflate(layoutInflater)
    }

    val viewModel by viewModels<ThemePreviewViewModel>()

    val loadDialog by lazy {
        LoadingWithAdDialog(this).apply {
            setCancelable(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)

        var resources = GsonUtil.gson.fromJson(
            intent.getStringExtra(sKeyResource),
            Resources::class.java
        )
        viewModel.resources = resources

        GlideUtil.load(viewBinding.themeIv, resources.previewUrl.withHost())

        viewBinding.backIv.setOnClickListener {
            finish()
            AdUtil.showAd(AdName.interleaving)
        }
        viewBinding.setTv.noDoubleClick {

            if (VipManager.isVip.value == true) {
                viewModel.download(this@ThemePreviewActivity)
                return@noDoubleClick
            }

            if (AdUtil.isReady(AdName.unlock)) {
                if (!AdUtil.isEnable(AdName.unlock)){
                    viewModel.download(this@ThemePreviewActivity)
                    return@noDoubleClick
                }

                AdUtil.showAd(AdName.unlock, object : IAdCallBack {
                    override fun onNoReady() {

                    }

                    override fun onAdClose(status: Int) {
                        if (status == LambdaAd.AD_CLOSE_REWARD_COMPLETE || status == LambdaAd.AD_CLOSE) {
                            viewModel.download(this@ThemePreviewActivity)
                        }
                    }
                })
            } else {
                VipActivity.start(this, VipActivity.FromPreviewDownload)
            }
            logEvent(EventName.downloadClick, Bundle().apply {
                putString("id", viewModel.resources?.id ?: "")
            })
        }

        viewModel.loadDialogLiveData.observe(this, Observer {
            if (it) {
                loadDialog.show()
            } else {
                loadDialog.dismiss()
            }
        })

        logEvent(EventName.previewPageView, Bundle().apply {
            putString("id", viewModel.resources?.id ?: "")
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        AdUtil.showAd(AdName.interleaving)
    }
}