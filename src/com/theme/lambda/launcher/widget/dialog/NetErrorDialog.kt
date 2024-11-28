package com.theme.lambda.launcher.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.android.launcher3.R
import com.android.launcher3.databinding.DialogNetErrorBinding
import com.lambda.common.utils.CommonUtil

class NetErrorDialog(context: Context) : Dialog(context, R.style.Theme_translucentDialog) {

    val viewBinding = DialogNetErrorBinding.inflate(LayoutInflater.from(context))

    var clickConnectNewListen: (() -> Unit)? = null
    var clickRetryListen: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(viewBinding.root)

        val params = window?.attributes
        params?.width = CommonUtil.dp2px(330f)
        window?.attributes = params

        setCanceledOnTouchOutside(false)
        setCancelable(false)

        viewBinding.connectNowTv.setOnClickListener {
            clickConnectNewListen?.invoke()
        }
        viewBinding.retryTv.setOnClickListener {
            clickRetryListen?.invoke()
        }
    }
}