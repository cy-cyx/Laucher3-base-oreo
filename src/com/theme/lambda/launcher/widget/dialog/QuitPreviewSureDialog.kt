package com.theme.lambda.launcher.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.android.launcher3.R
import com.android.launcher3.databinding.DialogQuitPreviewSureBinding
import com.theme.lambda.launcher.utils.CommonUtil

class QuitPreviewSureDialog(context: Context) : Dialog(context, R.style.Theme_translucentDialog) {

    val viewBinding = DialogQuitPreviewSureBinding.inflate(LayoutInflater.from(context))
    var onClickContinueListen: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        val params = window?.attributes
        params?.width = CommonUtil.dp2px(330f)
        window?.attributes = params

        viewBinding.cancelTv.setOnClickListener {
            dismiss()
        }
        viewBinding.continueTv.setOnClickListener {
            dismiss()
            onClickContinueListen?.invoke()
        }
    }
}