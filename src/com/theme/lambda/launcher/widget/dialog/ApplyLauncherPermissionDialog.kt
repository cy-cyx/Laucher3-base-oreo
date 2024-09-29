package com.theme.lambda.launcher.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.android.launcher3.R
import com.android.launcher3.databinding.DialogApplyLauncherPermissionBinding
import com.theme.lambda.launcher.utils.CommonUtil

class ApplyLauncherPermissionDialog(context: Context) :
    Dialog(context, R.style.Theme_translucentDialog) {

    val viewBinding = DialogApplyLauncherPermissionBinding.inflate(LayoutInflater.from(context))

    var clickApplyListen: (() -> Unit)? = null
    var clickNotNowListen: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(viewBinding.root)

        val params = window?.attributes
        params?.width = CommonUtil.dp2px(320f)
        window?.attributes = params

        setCancelable(false)
        setCanceledOnTouchOutside(false)

        viewBinding.titleTv.text =
            context.getString(R.string.set_default_home_app, context.getString(R.string.app_name))

        viewBinding.allowTv.setOnClickListener {
            clickApplyListen?.invoke()
        }

        viewBinding.notNowTv.setOnClickListener {
            clickNotNowListen?.invoke()
        }
    }
}