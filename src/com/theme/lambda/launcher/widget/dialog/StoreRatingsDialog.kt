package com.theme.lambda.launcher.widget.dialog

import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.android.launcher3.Launcher
import com.android.launcher3.R
import com.android.launcher3.databinding.DialogStoreRatingDialogBinding
import com.lambda.common.base.BaseDialog
import com.lambda.common.utils.CommonUtil
import com.lambda.common.utils.getSpBool
import com.lambda.common.utils.gone
import com.lambda.common.utils.putSpBool
import com.lambda.common.utils.visible
import com.theme.lambda.launcher.utils.RateUtil
import com.lambda.common.utils.SpKey

class StoreRatingsDialog(context: Context) : BaseDialog(context, R.style.Theme_translucentDialog) {

    companion object {
        @JvmStatic
        fun show(context: Context) {
            if (SpKey.hasStoreRatings.getSpBool()) return
            if (context is Launcher) {
                context.showDialogOnQueue(StoreRatingsDialog(context))
            } else {
                StoreRatingsDialog(context).show()
            }
        }
    }

    val viewBinding = DialogStoreRatingDialogBinding.inflate(LayoutInflater.from(context))

    private var star = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(viewBinding.root)

        val params = window?.attributes
        params?.width = CommonUtil.dp2px(320f)
        window?.attributes = params

        setCanceledOnTouchOutside(false)

        viewBinding.guideLav.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                viewBinding.guideLav.gone()
                viewBinding.starLl.visible()
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        viewBinding.guideLav.playAnimation()

        viewBinding.starIv1.setOnClickListener {
            upDataStar(1)
        }

        viewBinding.starIv2.setOnClickListener {
            upDataStar(2)
        }

        viewBinding.starIv3.setOnClickListener {
            upDataStar(3)
        }

        viewBinding.starIv4.setOnClickListener {
            upDataStar(4)
        }

        viewBinding.starIv5.setOnClickListener {
            upDataStar(5)
        }

        viewBinding.submitTv.setOnClickListener {
            if (star == 0) return@setOnClickListener
            if (star > 3) {
                RateUtil.rate(context)
            } else {
                RateUtil.contactUs(context)
            }
            dismiss()
            SpKey.hasStoreRatings.putSpBool(true)
        }
        viewBinding.submitTv.alpha = 0.3f

        viewBinding.closeIv.setOnClickListener {
            dismiss()
        }
    }

    private fun upDataStar(num: Int) {
        when (num) {
            1 -> {
                viewBinding.starIv1.setImageResource(R.drawable.ic_star)
                viewBinding.starIv2.setImageResource(R.drawable.ic_star_gray)
                viewBinding.starIv3.setImageResource(R.drawable.ic_star_gray)
                viewBinding.starIv4.setImageResource(R.drawable.ic_star_gray)
                viewBinding.starIv5.setImageResource(R.drawable.ic_star_gray)
            }

            2 -> {
                viewBinding.starIv1.setImageResource(R.drawable.ic_star)
                viewBinding.starIv2.setImageResource(R.drawable.ic_star)
                viewBinding.starIv3.setImageResource(R.drawable.ic_star_gray)
                viewBinding.starIv4.setImageResource(R.drawable.ic_star_gray)
                viewBinding.starIv5.setImageResource(R.drawable.ic_star_gray)
            }

            3 -> {
                viewBinding.starIv1.setImageResource(R.drawable.ic_star)
                viewBinding.starIv2.setImageResource(R.drawable.ic_star)
                viewBinding.starIv3.setImageResource(R.drawable.ic_star)
                viewBinding.starIv4.setImageResource(R.drawable.ic_star_gray)
                viewBinding.starIv5.setImageResource(R.drawable.ic_star_gray)
            }

            4 -> {
                viewBinding.starIv1.setImageResource(R.drawable.ic_star)
                viewBinding.starIv2.setImageResource(R.drawable.ic_star)
                viewBinding.starIv3.setImageResource(R.drawable.ic_star)
                viewBinding.starIv4.setImageResource(R.drawable.ic_star)
                viewBinding.starIv5.setImageResource(R.drawable.ic_star_gray)
            }

            5 -> {
                viewBinding.starIv1.setImageResource(R.drawable.ic_star)
                viewBinding.starIv2.setImageResource(R.drawable.ic_star)
                viewBinding.starIv3.setImageResource(R.drawable.ic_star)
                viewBinding.starIv4.setImageResource(R.drawable.ic_star)
                viewBinding.starIv5.setImageResource(R.drawable.ic_star)
            }
        }
        star = num
        viewBinding.submitTv.alpha = 1f
    }
}