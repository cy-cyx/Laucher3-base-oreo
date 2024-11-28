package com.theme.lambda.launcher.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.android.launcher3.R
import com.android.launcher3.databinding.DialogSetDefaultFailedBinding
import com.lambda.common.statistics.EventName
import com.lambda.common.statistics.EventUtil
import com.lambda.common.utils.CommonUtil
import com.theme.lambda.launcher.utils.LauncherUtil

class SetDefaultFailedDialog(context: Context) : Dialog(context, R.style.Theme_translucentDialog) {

    val viewBinding = DialogSetDefaultFailedBinding.inflate(LayoutInflater.from(context))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(viewBinding.root)

        val params = window?.attributes
        params?.width = CommonUtil.dp2px(330f)
        window?.attributes = params

        setCanceledOnTouchOutside(false)

        viewBinding.tryAgainTv.setOnClickListener {
            dismiss()
            LauncherUtil.gotoSetLauncher(context)
        }
        viewBinding.doNotTv.setOnClickListener {
            dismiss()
            EventUtil.logEvent(EventName.permissionFeedback, Bundle())
            Toast.makeText(context, R.string.feedback_sent, Toast.LENGTH_SHORT).show()
        }
        viewBinding.lateTv.setOnClickListener {
            dismiss()
        }
    }
}