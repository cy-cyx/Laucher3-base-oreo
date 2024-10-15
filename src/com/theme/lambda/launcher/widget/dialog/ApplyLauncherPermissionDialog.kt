package com.theme.lambda.launcher.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.android.launcher3.R
import com.android.launcher3.databinding.DialogApplyLauncherPermissionBinding
import com.theme.lambda.launcher.statistics.EventName
import com.theme.lambda.launcher.statistics.EventUtil.logEvent
import com.theme.lambda.launcher.utils.CommonUtil

class ApplyLauncherPermissionDialog(context: Context) :
    Dialog(context, R.style.Theme_translucentDialog) {

    val viewBinding = DialogApplyLauncherPermissionBinding.inflate(LayoutInflater.from(context))

    var clickApplyListen: (() -> Unit)? = null
    var clickNotNowListen: (() -> Unit)? = null

    var from = sFromHome

    companion object {
        const val sFromHome = "home"
        const val sFromDetail = "detail"
    }

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
            logEvent(EventName.permissionDialogClick, Bundle().apply {
                putString("scene", from)
                putString("permission", "launcher")
                putString("type", "allow")
            })
        }

        viewBinding.notNowTv.setOnClickListener {
            clickNotNowListen?.invoke()
            logEvent(EventName.permissionDialogClick, Bundle().apply {
                putString("scene", from)
                putString("permission", "launcher")
                putString("type", "dismiss")
            })
        }

        logEvent(EventName.permissionDialogShow, Bundle().apply {
            putString("scene", from)
            putString("permission", "launcher")
        })
    }
}