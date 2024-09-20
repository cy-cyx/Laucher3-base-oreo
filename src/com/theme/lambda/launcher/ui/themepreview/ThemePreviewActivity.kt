package com.theme.lambda.launcher.ui.themepreview

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.android.launcher3.databinding.ActivityThemePreviewBinding
import com.theme.lambda.launcher.base.BaseActivity
import com.theme.lambda.launcher.data.model.Resources
import com.theme.lambda.launcher.utils.GlideUtil
import com.theme.lambda.launcher.utils.GsonUtil
import com.theme.lambda.launcher.utils.StatusBarUtil
import com.theme.lambda.launcher.utils.withHost
import com.theme.lambda.launcher.widget.dialog.LoadingDialog

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
        LoadingDialog(this).apply {
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
        }
        viewBinding.setTv.setOnClickListener {
            viewModel.download(this@ThemePreviewActivity)
        }

        viewModel.loadDialogLiveData.observe(this, Observer {
            if (it) {
                loadDialog.show()
            } else {
                loadDialog.dismiss()
            }
        })
    }
}