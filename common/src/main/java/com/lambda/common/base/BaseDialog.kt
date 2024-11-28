package com.lambda.common.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface

/**
 * 继承该基类可以对话框队列
 */
open class BaseDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {

    init {
        super.setOnDismissListener {
            dismissListenProxy?.onDismiss(it)
            dialogManagerDismissListener?.onDismiss(it)
        }
    }

    // 由于避免该方法被使用，顶掉对话框队列的监听
    private var dismissListenProxy: DialogInterface.OnDismissListener? = null
    private var dialogManagerDismissListener: DialogInterface.OnDismissListener? = null

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        dismissListenProxy = listener
    }

    // 对话框队列专用
    fun setDialogManagerDismissListener(listener: DialogInterface.OnDismissListener?) {
        dialogManagerDismissListener = listener
    }
}