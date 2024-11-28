package com.theme.lambda.launcher.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.android.launcher3.R
import com.lambda.common.utils.CommonUtil

class TimingProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()

    private val viewWidth = CommonUtil.dp2px(280f)
    private val viewHeight = CommonUtil.dp2px(14f)

    private var progress = .0f


    var anim: ValueAnimator? = null
    fun startProgress(duration: Long, callback: (() -> Unit)) {
        anim?.removeAllUpdateListeners()
        anim?.pause()
        anim = ValueAnimator.ofFloat(0f, 1f)
        anim?.duration = duration
        anim?.addUpdateListener {
            progress = it.animatedValue as Float
            callback.invoke()
            postInvalidate()
        }
        anim?.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(viewWidth, viewHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.style = Paint.Style.FILL
        paint.strokeWidth = viewHeight / 2f
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND
        paint.color = CommonUtil.getColor(R.color.color_cbcbcb)
        canvas?.drawLine(
            viewHeight / 2f,
            viewHeight / 2f,
            viewWidth - viewHeight / 2f,
            viewHeight / 2f,
            paint
        )

        paint.color = CommonUtil.getColor(R.color.color_0e7fff)
        canvas?.drawLine(
            viewHeight / 2f,
            viewHeight / 2f,
            viewHeight / 2f + (viewWidth - viewHeight) * progress,
            viewHeight / 2f,
            paint
        )
    }
}