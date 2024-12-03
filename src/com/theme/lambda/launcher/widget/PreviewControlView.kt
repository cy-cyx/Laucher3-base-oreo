package com.theme.lambda.launcher.widget

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.android.launcher3.ThemeManager
import com.android.launcher3.databinding.LayoutPreviewControlBinding
import com.lambda.common.statistics.EventName
import com.lambda.common.statistics.EventUtil
import com.theme.lambda.launcher.ui.effect.EffectActivity
import com.theme.lambda.launcher.ui.layoutadjust.LayoutAdjustActivity
import com.lambda.common.utils.CommonUtil
import com.lambda.common.utils.getSpBool
import com.lambda.common.utils.gone
import com.lambda.common.utils.putSpBool
import com.lambda.common.utils.visible
import com.theme.lambda.launcher.utils.LauncherUtil
import com.lambda.common.utils.SpKey
import com.theme.lambda.launcher.utils.SystemUtil

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
        layoutPreviewControlBinding?.setEffectIv?.setOnClickListener {
            EffectActivity.start(context)
        }
        layoutPreviewControlBinding?.setLayoutIv?.setOnClickListener {
            LayoutAdjustActivity.start(context)
        }

        // 处理兼容性问题 已经知道华为安卓10以上
        if (!LauncherUtil.isDefaultLauncher(context)
            && (SystemUtil.getDeviceBrand().equals(SystemUtil.PHONE_HUAWEI,true)
                    || SystemUtil.getDeviceBrand().equals(SystemUtil.PHONE_HONOR,true) ||
                    SystemUtil.getDeviceBrand().equals(SystemUtil.PHONE_NOVA,true))
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        ) {
            layoutPreviewControlBinding?.setLl?.gone()
            layoutPreviewControlBinding?.setNoApplyLl?.visible()
            EventUtil.logEvent(EventName.setIconEnable, Bundle().apply {
                putString("id", ThemeManager.getThemeManagerIfExist()?.previewThemeId)
            })
        } else {
            layoutPreviewControlBinding?.setLl?.visible()
            layoutPreviewControlBinding?.setNoApplyLl?.gone()
            startGuide()
        }

    }

    private fun startGuide() {
        if (SpKey.first_guide_1.getSpBool(false)) return
        SpKey.first_guide_1.putSpBool(true)

        layoutPreviewControlBinding?.guideVw?.visible()
        layoutPreviewControlBinding?.setLayoutGuideLl?.visible()
        layoutPreviewControlBinding?.guideVw?.setOnClickListener {
            if (layoutPreviewControlBinding?.setLayoutGuideLl?.visibility == View.VISIBLE) {
                layoutPreviewControlBinding?.setLayoutGuideLl?.gone()
                layoutPreviewControlBinding?.setEffectGuideLl?.visible()
            } else {
                layoutPreviewControlBinding?.setEffectGuideLl?.gone()
                layoutPreviewControlBinding?.guideVw?.gone()
            }
        }
    }


    interface ControlListen {

        fun onCancel()

        fun onSet()

        fun setIcon()
    }

    fun reLayout() {
        val lp = this.layoutParams as FrameLayout.LayoutParams
        lp.topMargin = -CommonUtil.getStatusBarHeight()
        lp.bottomMargin = -CommonUtil.getActionBarHeight(context)
        requestLayout()
    }
}