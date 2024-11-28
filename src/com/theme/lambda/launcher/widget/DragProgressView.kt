package com.theme.lambda.launcher.widget

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.android.launcher3.R
import com.lambda.common.utils.CommonUtil

class DragProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var viewWidth = CommonUtil.dp2px(200f)
    private val viewHeight = CommonUtil.dp2px(40f)
    private val lineWidth = CommonUtil.dp2px(5f)

    private var curProgress = .5f

    private val paint = Paint()

    var callback: ((Float) -> Unit)? = null

    private var bm = BitmapFactory.decodeResource(resources, R.drawable.ic_blue_bar)
    private var bmRect = Rect(0, 0, bm.width, bm.height)
    private var dstRect = RectF()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(viewWidth, viewHeight)
    }

    fun setProgress(p: Float) {
        if (isMoving) return
        curProgress = p
        invalidate()
    }

    private var isMoving = false

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        parent.requestDisallowInterceptTouchEvent(true)

        if (event?.action == MotionEvent.ACTION_DOWN) {
            isMoving = true
        }
        if (event?.action == MotionEvent.ACTION_UP) {
            isMoving = false
        }

        when (event?.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE,
            MotionEvent.ACTION_UP -> {
                val x = event.x

                if (x > viewWidth - viewHeight / 2) {
                    curProgress = 1f
                } else if (x < viewHeight / 2) {
                    curProgress = 0f
                } else {
                    curProgress = (x - viewHeight / 2) / (viewWidth - viewHeight).toFloat()
                }

                if (event.action == MotionEvent.ACTION_UP) {
                    callback?.invoke(curProgress)
                }

                invalidate()
                return true
            }
        }


        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.reset()
        paint.strokeWidth = lineWidth.toFloat()
        paint.strokeCap = Paint.Cap.ROUND
        paint.color = CommonUtil.getColor(R.color.color_e2e2e2)
        canvas.drawLine(
            viewHeight / 2f,
            viewHeight / 2f,
            viewWidth - viewHeight / 2f,
            viewHeight / 2f,
            paint
        )

        paint.reset()
        paint.strokeWidth = CommonUtil.dp2px(2f).toFloat()
        paint.strokeCap = Paint.Cap.ROUND
        paint.color = CommonUtil.getColor(R.color.color_e2e2e2)
        canvas.drawLine(
            viewWidth / 2f,
            viewHeight / 2f - CommonUtil.dp2px(5f),
            viewWidth / 2f,
            viewHeight / 2f + CommonUtil.dp2px(5f),
            paint
        )

        paint.reset()
        paint.strokeWidth = lineWidth.toFloat()
        paint.strokeCap = Paint.Cap.ROUND
        paint.color = CommonUtil.getColor(R.color.color_208af4)
        val x2 = (viewWidth - viewHeight) * curProgress + viewHeight / 2f
        canvas.drawLine(
            viewHeight / 2f,
            viewHeight / 2f,
            x2,
            viewHeight / 2f,
            paint
        )

        paint.reset()
        dstRect.set(
            x2 - CommonUtil.dp2px(14f),
            viewHeight / 2f - CommonUtil.dp2px(14f),
            x2 + CommonUtil.dp2px(14f),
            viewHeight / 2f + CommonUtil.dp2px(14f)
        )
        canvas.drawBitmap(bm, bmRect, dstRect, paint)
    }
}