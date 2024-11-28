package com.lambda.common.base

import android.content.DialogInterface
import java.util.concurrent.ConcurrentLinkedQueue

class DialogManager : DialogInterface.OnDismissListener {

    private var curShowingDialog: BaseDialog? = null

    private var dialogQueue = ConcurrentLinkedQueue<BaseDialog>()

    fun showDialogOnQueue(dialog: BaseDialog) {
        dialogQueue.offer(dialog)
        tryShowNext()
    }

    private fun tryShowNext() {
        if (curShowingDialog != null) return
        dialogQueue.poll()?.let {
            show(it)
        }
    }

    private fun show(dialog: BaseDialog) {
        dialog.setDialogManagerDismissListener(this)
        try {
            dialog.show()
            curShowingDialog = dialog
        } catch (e: Exception) {
            // 尝试展示下一个
            tryShowNext()
        }
    }

    override fun onDismiss(p0: DialogInterface?) {
        curShowingDialog = null
        tryShowNext()
    }

}