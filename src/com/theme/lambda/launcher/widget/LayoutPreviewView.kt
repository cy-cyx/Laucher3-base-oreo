package com.theme.lambda.launcher.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.theme.lambda.launcher.utils.CommonUtil

class LayoutPreviewView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var width = 0
    private var height = 0

    private var column = 6
    private var row = 6

    // 最小的间隔
    private var interval = CommonUtil.dp2px(10f)

    // 最大的块的宽
    private var maxPieceWidth = 0

    private var paint = Paint()

    init {
        paint.isAntiAlias = true
        paint.setColor(Color.parseColor("#FFA4D2FF"))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        width = MeasureSpec.getSize(widthMeasureSpec)
        height = MeasureSpec.getSize(heightMeasureSpec)

        maxPieceWidth = (height - interval * 5) / 6
    }


    fun setData(r: Int, c: Int) {
        row = r
        column = c
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var pieceWidth = (width - interval * (row - 1)) / row
        if (pieceWidth > maxPieceWidth) {
            pieceWidth = maxPieceWidth
        }

        var partX = width / row
        var partY = height / column


        for (c in 0 until column) {
            for (r in 0 until row) {

                var centerX = partX * r + partX / 2f
                var centerY = partY * c + partY / 2f
                drawPiece(canvas, centerX, centerY, pieceWidth)
            }
        }
    }

    private fun drawPiece(canvas: Canvas, x: Float, y: Float, pieceWidth: Int) {
        var px = x - pieceWidth / 2f
        var py = y - pieceWidth / 2f

        canvas.drawRoundRect(px, py, px + pieceWidth, py + pieceWidth, 9f, 9f, paint)
    }
}