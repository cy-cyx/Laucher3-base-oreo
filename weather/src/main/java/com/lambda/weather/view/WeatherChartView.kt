package com.lambda.weather.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.lambda.weather.R
import com.lambdaweather.utils.ScreenUtils


class WeatherChartView@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs,defStyleAttr) {
    /**
     * x轴集合
     */
    private var mXAxis: FloatArray = floatArrayOf()

    /**
     * 白天y轴集合
     */
    private var mYAxisDay: FloatArray = floatArrayOf()

    /**
     * 夜间y轴集合
     */
    private var mYAxisNight: FloatArray = floatArrayOf()

    /**
     * x,y轴集合数
     */
    private var LENGTH = 0

    /**
     * 白天温度集合
     */
    private var mTempDay: IntArray = intArrayOf()

    /**
     * 夜间温度集合
     */
    private var mTempNight: IntArray = intArrayOf()

    /**
     * 控件高
     */
    private var mHeight = 0

    /**
     * 字体大小
     */
    private var mTextSize = 0f

    /**
     * 圓半径
     */
    private var mRadius = 0f

    /**
     * 圓半径今天
     */
    private var mRadiusToday = 0f

    /**
     * 文字移动位置距离
     */
    private var mTextSpace = 0f

    /**
     * 线的大小
     */
    private var mStokeWidth = 0f

    /**
     * 白天折线颜色
     */
    private val mColorDay: Int = Color.parseColor("#1C99F1")

    /**
     * 夜间折线颜色
     */
    private val mColorNight: Int = Color.parseColor("#1C99F1")

    /**
     * 字体颜色
     */
    private val mTextColor: Int = Color.parseColor("#111723")

    /**
     * 屏幕密度
     */
    private var mDensity = 0f
    private var isDrawNight = true

    /**
     * 控件边的空白空间
     */
    private val mSpace = 0f
    private var mUnitStr = ""
    init {
        mDensity = resources.displayMetrics.density
        mRadius = 3 * mDensity
        mRadiusToday = 3 * mDensity
        mTextSpace = 10 * mDensity
        mStokeWidth = 2 * mDensity
        mTextSize = ScreenUtils.dp2px(context, 12).toFloat()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 设置控件高度，x轴集合
        setHeightAndXAxis()
        // 计算y轴集合数值
        computeYAxisValues()
        // 画白天折线图
        drawChart(canvas, mColorDay, mTempDay, mYAxisDay, 0)
        // 画夜间折线图
        if (isDrawNight) {
            drawChart(canvas, mColorNight, mTempNight, mYAxisNight, 1)
        }
    }

    /**
     * 计算y轴集合数值
     */
    private fun computeYAxisValues() {
        if(mTempDay.isEmpty()) return
        // 存放白天最低温度
        var minTempDay = mTempDay[0]
        // 存放白天最高温度
        var maxTempDay = mTempDay[0]
        for (item in mTempDay) {
            if (item < minTempDay) {
                minTempDay = item
            }
            if (item > maxTempDay) {
                maxTempDay = item
            }
        }

        // 存放夜间最低温度
        var minTempNight = mTempNight[0]
        // 存放夜间最高温度
        var maxTempNight = mTempNight[0]
        for (item in mTempNight) {
            if (item < minTempNight) {
                minTempNight = item
            }
            if (item > maxTempNight) {
                maxTempNight = item
            }
        }

        // 白天，夜间中的最低温度
        val minTemp = if (minTempNight < minTempDay) minTempNight else minTempDay
        // 白天，夜间中的最高温度
        val maxTemp = if (maxTempDay > maxTempNight) maxTempDay else maxTempNight

        // 份数（白天，夜间综合温差）
        val parts = (maxTemp - minTemp).toFloat()
        // y轴一端到控件一端的距离
        val length = mSpace + mTextSize + mTextSpace + mRadius
        // y轴高度
        val yAxisHeight = mHeight - length * 2

        // 当温度都相同时（被除数不能为0）
        if (parts == 0f) {
            for (i in 0 until LENGTH) {
                mYAxisDay[i] = yAxisHeight / 2 + length
                mYAxisNight[i] = yAxisHeight / 2 + length
            }
        } else {
            val partValue = yAxisHeight / parts
            for (i in 0 until LENGTH) {
                mYAxisDay[i] = mHeight - partValue * (mTempDay[i] - minTemp) - length
                mYAxisNight[i] = mHeight - partValue * (mTempNight[i] - minTemp) - length
            }
        }
    }

    /**
     * 画折线图
     *
     * @param canvas 画布
     * @param color  画图颜色
     * @param temp   温度集合
     * @param yAxis  y轴集合
     * @param type   折线种类：0，白天；1，夜间
     */
    private fun drawChart(
        canvas: Canvas,
        color: Int,
        temp: IntArray,
        yAxis: FloatArray,
        type: Int
    ) {
        var color = color
        // 线画笔
        val linePaint = Paint()
        // 抗锯齿
        linePaint.isAntiAlias = true
        // 线宽
        linePaint.strokeWidth = mStokeWidth
        linePaint.color = color
        // 空心
        linePaint.style = Paint.Style.STROKE

        // 点画笔
        val pointPaint = Paint()
        pointPaint.isAntiAlias = true
        pointPaint.color = color

        val pointPaint2 = Paint()
        pointPaint2.isAntiAlias = true
        pointPaint2.color = ContextCompat.getColor(context, R.color.color_00ffff)

        // 字体画笔
        val textPaint = Paint()
        textPaint.isAntiAlias = true
        textPaint.color = mTextColor
        textPaint.textSize = mTextSize
        // 文字居中
        textPaint.textAlign = Paint.Align.CENTER
        val alpha1 = 255
        val alpha2 = 255
        for (i in 0 until LENGTH) {
            // 画线
            if (i < LENGTH - 1) {
                // 昨天
                if (i == -1) {
                    // 设置虚线效果
                    linePaint.pathEffect = DashPathEffect(
                        floatArrayOf(
                            2 * mDensity,
                            2 * mDensity
                        ), 0f
                    )
                    // 路径
                    val path = Path()
                    // 路径起点
                    path.moveTo(mXAxis[i], yAxis[i])
                    // 路径连接到
                    path.lineTo(mXAxis[i + 1], yAxis[i + 1])
                    canvas.drawPath(path, linePaint)
                } else {
                    if (type == 0) {
                        linePaint.pathEffect = null
                        linePaint.style = Paint.Style.STROKE //设置实心
                        var path = Path() //Path对象
                        path.moveTo(mXAxis[i], mYAxisDay[i]) //起始点
                        path.lineTo(mXAxis[i + 1], mYAxisDay[i + 1]) //连线到下一点
                        canvas.drawPath(path, linePaint) //绘制任意多边形
                        path = Path()
                        path.moveTo(mXAxis[i], mYAxisNight[i]) //起始点
                        path.lineTo(mXAxis[i + 1], mYAxisNight[i + 1]) //连线到下一点
//                        path.lineTo(mXAxis[i], mYAxisNight[i]) //连线到下一点
                        canvas.drawPath(path, linePaint) //绘制任意多边形
//                        path.lineTo(mXAxis[i], mYAxisDay[i]) //连线到下一点
//                        canvas.drawPath(path, linePaint) //绘制任意多边形
                    }
                    //canvas.drawLine(mXAxis[i], yAxis[i], mXAxis[i + 1], yAxis[i + 1], linePaint);
                }
            }

            // 画点
            if (i != 1) {
                // 昨天
                if (i == 0) {
                    pointPaint2.alpha = alpha2
                    canvas.drawCircle(mXAxis[i], yAxis[i], mRadius, pointPaint2)
                } else {
                    pointPaint.alpha = alpha1
                    canvas.drawCircle(mXAxis[i], yAxis[i], mRadius, pointPaint)
                }
                // 今天
            } else {
                pointPaint.alpha = alpha1
                canvas.drawCircle(mXAxis[i], yAxis[i], mRadiusToday, pointPaint)
            }

            // 画字
            // 昨天
            if (i == 0 || i == LENGTH - 1) {
                textPaint.alpha = alpha1
                drawText(canvas, textPaint, i, temp, yAxis, type)
            } else {
                textPaint.alpha = alpha2
                drawText(canvas, textPaint, i, temp, yAxis, type)
            }
        }
    }

    /**
     * 绘制文字
     *
     * @param canvas    画布
     * @param textPaint 画笔
     * @param i         索引
     * @param temp      温度集合
     * @param yAxis     y轴集合
     * @param type      折线种类：0，白天；1，夜间
     */
    private fun drawText(
        canvas: Canvas,
        textPaint: Paint,
        i: Int,
        temp: IntArray,
        yAxis: FloatArray,
        type: Int
    ) {
        when (type) {
            0 ->                 // 显示白天气温
                canvas.drawText(
                    temp[i].toString() + mUnitStr,
                    mXAxis[i], yAxis[i] - mRadius - mTextSpace, textPaint
                )
            1 ->                 // 显示夜间气温
                canvas.drawText(
                    temp[i].toString() + mUnitStr,
                    mXAxis[i], yAxis[i] + mTextSpace + mTextSize, textPaint
                )
        }
    }

    /**
     * 设置高度，x轴集合
     */
    private fun setHeightAndXAxis() {
        mHeight = height
        // 控件宽
        val width = width
        val i = if(LENGTH == 0) 1 else LENGTH
        // 每一份宽
        val w = (width / i).toFloat()
        for (j in 0 until LENGTH) {
            when (j) {
                0 -> {
                    mXAxis[j] = (0f + w) / 2
                }
                LENGTH - 1 -> {
                    mXAxis[j] = ((LENGTH - 1) * w) + (w * 1) /2
                }
                else -> {
                    mXAxis[j] = w * j + (w * 1) /2
                }
            }
        }
    }

    /**
     * 设置白天温度
     *
     * @param tempDay 温度数组集合
     */
    fun setTempDay(tempDay: IntArray) {
        mTempDay = tempDay
        LENGTH = mTempDay.size
        mXAxis = FloatArray(LENGTH)
        mYAxisDay = FloatArray(LENGTH)
        mYAxisNight = FloatArray(LENGTH)
    }

    /**
     * 设置夜间温度
     *
     * @param tempNight 温度数组集合
     */
    fun setTempNight(tempNight: IntArray) {
        mTempNight = tempNight
    }

    /**
     * 设置白天曲线的颜色
     */
    fun setColorDay() {}

    fun setUnit(unit: String) {
        mUnitStr = unit
    }

    fun setDrawNight() {
        isDrawNight = false
    }
}