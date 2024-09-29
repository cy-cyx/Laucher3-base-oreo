package com.theme.lambda.launcher.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.android.launcher3.databinding.LayoutPreviewControlBinding
import com.theme.lambda.launcher.utils.LauncherUtil
import com.theme.lambda.launcher.utils.visible

class PreviewControlView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    var controlListen: ControlListen? = null

    var layoutPreviewControlBinding: LayoutPreviewControlBinding? = null

    init {

        layoutPreviewControlBinding =
            LayoutPreviewControlBinding.inflate(LayoutInflater.from(context), this, true)

        layoutPreviewControlBinding?.backIv?.setOnClickListener {
            controlListen?.onCancel()
        }
        layoutPreviewControlBinding?.setTv?.setOnClickListener {
            controlListen?.onSet()
        }

        if (!LauncherUtil.isDefaultLauncher(context)) {
            layoutPreviewControlBinding?.applyFl?.visible()
            layoutPreviewControlBinding?.applyFl?.setOnClickListener {
                LauncherUtil.gotoSetLauncher(context)
            }
        }
    }


    interface ControlListen {

        fun onCancel()

        fun onSet()
    }
}