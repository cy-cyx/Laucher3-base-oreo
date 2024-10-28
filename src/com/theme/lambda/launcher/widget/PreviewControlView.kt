package com.theme.lambda.launcher.widget

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.android.launcher3.ThemeManager
import com.android.launcher3.databinding.LayoutPreviewControlBinding
import com.theme.lambda.launcher.statistics.EventName
import com.theme.lambda.launcher.statistics.EventUtil
import com.theme.lambda.launcher.utils.LauncherUtil
import com.theme.lambda.launcher.utils.SystemUtil
import com.theme.lambda.launcher.utils.gone
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
        layoutPreviewControlBinding?.setHomeScreenTv?.setOnClickListener {
            controlListen?.onSet()
        }
        layoutPreviewControlBinding?.setIconTv?.setOnClickListener {
            controlListen?.setIcon()
        }

        // 处理兼容性问题 已经知道华为安卓10以上
        if (!LauncherUtil.isDefaultLauncher(context) && SystemUtil.getDeviceBrand() == SystemUtil.PHONE_HUAWEI && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            layoutPreviewControlBinding?.setTv?.gone()
            layoutPreviewControlBinding?.setLl?.visible()
            EventUtil.logEvent(EventName.setIconEnable, Bundle().apply {
                putString("id", ThemeManager.getThemeManagerIfExist()?.previewThemeId)
            })
        } else {
            layoutPreviewControlBinding?.setTv?.visible()
            layoutPreviewControlBinding?.setLl?.gone()
        }

    }


    interface ControlListen {

        fun onCancel()

        fun onSet()

        fun setIcon()
    }
}