package com.theme.lambda.launcher.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.android.launcher3.R
import com.android.launcher3.databinding.DialogLaodingBinding
import com.theme.lambda.launcher.utils.CommonUtil

class LoadingDialog(context: Context) : Dialog(context, R.style.Theme_translucentDialog) {

    val viewBinding = DialogLaodingBinding.inflate(LayoutInflater.from(context))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        val params = window?.attributes
        params?.width = CommonUtil.dp2px(76f)
        params?.height = CommonUtil.dp2px(76f)
        window?.attributes = params

        setCanceledOnTouchOutside(false)
        setCancelable(false)
    }
}