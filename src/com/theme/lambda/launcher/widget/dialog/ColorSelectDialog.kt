package com.theme.lambda.launcher.widget.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import com.android.launcher3.R
import com.android.launcher3.databinding.DialogColorSelectBinding
import com.lambda.common.utils.CommonUtil
import com.lambda.common.utils.noDoubleClick
import top.defaults.colorpicker.ColorObserver

class ColorSelectDialog(context: Context) : Dialog(context, R.style.Theme_translucentDialog) {

    private val viewBinding = DialogColorSelectBinding.inflate(LayoutInflater.from(context))

    var colorSelectCallback: ((Int) -> Unit)? = null

    private var curColor = Color.WHITE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        val params = window?.attributes
        params?.width = CommonUtil.dp2px(360f)
        params?.height = CommonUtil.dp2px(450f)
        window?.attributes = params

        setCanceledOnTouchOutside(false)
        setCancelable(false)

        viewBinding.closeIv.setOnClickListener {
            dismiss()
        }

        viewBinding.okTv.noDoubleClick {
            dismiss()
            colorSelectCallback?.invoke(curColor)
        }

        viewBinding.colorPicker.subscribe(object : ColorObserver {
            override fun onColor(color: Int, fromUser: Boolean, shouldPropagate: Boolean) {
                curColor = color
            }
        })
    }

    fun setInitColor(color: Int) {
        viewBinding.colorPicker.setInitialColor(color)
        viewBinding.colorPicker.reset();
        curColor = color
    }
}