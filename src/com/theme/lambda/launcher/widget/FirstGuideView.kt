package com.theme.lambda.launcher.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.android.launcher3.databinding.ViewFirstGuideBinding
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.gone
import com.theme.lambda.launcher.utils.visible

class FirstGuideView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding = ViewFirstGuideBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        setOnClickListener {

        }
    }

    fun startGuide() {
        reset()
    }

    private fun reset() {
        val lp = this.layoutParams as FrameLayout.LayoutParams
        lp.topMargin = -CommonUtil.getStatusBarHeight()
        lp.bottomMargin = -CommonUtil.getActionBarHeight()
        requestLayout()

        showFirstStep()
    }

    private fun showFirstStep() {
        binding.step1Fl.visible()

        val layoutParams = binding.guideTextLl.layoutParams as FrameLayout.LayoutParams
        val guideTopMargin =
            (CommonUtil.getScreenHeight() - CommonUtil.dp2px(88f) - CommonUtil.dp2px(28f) - CommonUtil.getStatusBarHeight()) / 4f * 3f + CommonUtil.getStatusBarHeight()
        layoutParams.topMargin = guideTopMargin.toInt()
        binding.guideTextLl.requestLayout()
        binding.next1Tv.setOnClickListener {
            binding.step1Fl.gone()
            showSecondStep()
        }
    }

    private fun showSecondStep() {
        binding.step2Fl.visible()
        binding.next2Tv.setOnClickListener {
            gone()
        }
    }
}

class MaskHoleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val paint = Paint()

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)

        paint.reset()
        paint.setColor(Color.parseColor("#80000000"))
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        val guideTopMargin =
            (CommonUtil.getScreenHeight() - CommonUtil.dp2px(88f) - CommonUtil.dp2px(28f) - CommonUtil.getStatusBarHeight()) / 2f + CommonUtil.getStatusBarHeight()

        val guideHeight =
            (CommonUtil.getScreenHeight() - CommonUtil.dp2px(88f) - CommonUtil.dp2px(28f) - CommonUtil.getStatusBarHeight()) / 4f

        val guideWidth = CommonUtil.getScreenWidth() / 4f

        paint.setColor(Color.WHITE)
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        val rect = RectF(
            CommonUtil.dp2px(5f).toFloat(),
            guideTopMargin + guideHeight * 0.2f,
            guideWidth + CommonUtil.dp2px(5f),
            guideTopMargin + guideHeight - guideHeight * 0.2f
        )
        canvas.drawRoundRect(
            rect, CommonUtil.dp2px(10f).toFloat(),
            CommonUtil.dp2px(10f).toFloat(), paint
        )
    }
}