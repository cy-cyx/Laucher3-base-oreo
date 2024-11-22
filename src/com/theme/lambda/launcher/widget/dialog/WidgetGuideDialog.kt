package com.theme.lambda.launcher.widget.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.android.launcher3.Launcher
import com.android.launcher3.R
import com.android.launcher3.databinding.DialogWidgetGuideBinding
import com.theme.lambda.launcher.base.BaseDialog
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.widget.adapter.WidgetGuideAdapter

class WidgetGuideDialog(context: Context) : BaseDialog(context, R.style.Theme_translucentDialog) {

    companion object {
        @JvmStatic
        fun show(context: Context) {
            if (context is Launcher) {
                context.showDialogOnQueue(WidgetGuideDialog(context))
            } else {
                WidgetGuideDialog(context).show()
            }
        }
    }

    val viewBinding = DialogWidgetGuideBinding.inflate(LayoutInflater.from(context))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(viewBinding.root)

        val params = window?.attributes
        params?.width = CommonUtil.dp2px(320f)
        params?.height = CommonUtil.dp2px(378f)
        window?.attributes = params

        setCanceledOnTouchOutside(false)

        viewBinding.tipVp.adapter = WidgetGuideAdapter(context)
        viewBinding.tipVp.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 1) {
                    viewBinding.nextTv.setText(R.string.get)
                } else {
                    viewBinding.nextTv.setText(R.string.next)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
        viewBinding.indicator.setViewPager(viewBinding.tipVp)

        viewBinding.nextTv.setOnClickListener {
            if (viewBinding.tipVp.currentItem == 1) {
                dismiss()
            } else {
                viewBinding.tipVp.setCurrentItem(1, false)
            }

        }
    }
}