package com.lambdaweather.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.text.HtmlCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.R
import com.android.launcher3.databinding.ItemWeatherNewBinding
import com.lambdaweather.data.model.NewsModel.NewsDTO
import com.theme.lambda.launcher.ad.AdName
import com.theme.lambda.launcher.ad.view.MRECBanner
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.GlideUtil
import com.theme.lambda.launcher.utils.gone
import com.theme.lambda.launcher.utils.visible
import java.util.Random

class WeatherNewBanner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var mInterval: Int = 5 * 1000
    private var isShowIndicator: Boolean = false
    private var mSelectedDrawable: Drawable? = null
    private var mUnselectedDrawable: Drawable? = null
    private val mSize: Int
    private val mSpace: Int

    /**
     * 获取RecyclerView实例，便于满足自定义[RecyclerView.ItemAnimator]或者[RecyclerView.Adapter]的需求
     *
     * @return RecyclerView实例
     */
    lateinit var recyclerView: RecyclerView
    private val mLinearLayout: LinearLayout?
    private val adapter: RecyclerAdapter?
    private var onRvBannerClickListener: OnRvBannerClickListener? = null
    private val mData: MutableList<NewsDTO> = ArrayList()
    private var startX = 0
    private var startY = 0
    private var currentIndex = 0
    private var isPlaying = false
    private val handler = Handler()
    private var isTouched = false
    private var isAutoPlaying = true
    private val playTask: Runnable = object : Runnable {
        override fun run() {
            recyclerView.smoothScrollToPosition(++currentIndex)
            if (isShowIndicator) {
                switchIndicator()
            }
            handler.postDelayed(this, mInterval.toLong())
        }
    }
    private var mrecBanner: MRECBanner? = null

    var from = fromWeather

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerViewBanner)
        mInterval = a.getInt(R.styleable.RecyclerViewBanner_rvb_interval, 5000)
        isShowIndicator = a.getBoolean(R.styleable.RecyclerViewBanner_rvb_showIndicator, true)
        isAutoPlaying = a.getBoolean(R.styleable.RecyclerViewBanner_rvb_autoPlaying, true)
        val sd = a.getDrawable(R.styleable.RecyclerViewBanner_rvb_indicatorSelectedSrc)
        val usd = a.getDrawable(R.styleable.RecyclerViewBanner_rvb_indicatorUnselectedSrc)
        mSelectedDrawable = if (sd == null) {
            generateDefaultDrawable(DEFAULT_SELECTED_COLOR)
        } else {
            if (sd is ColorDrawable) {
                generateDefaultDrawable(sd.color)
            } else {
                sd
            }
        }
        mUnselectedDrawable = if (usd == null) {
            generateDefaultDrawable(DEFAULT_UNSELECTED_COLOR)
        } else {
            if (usd is ColorDrawable) {
                generateDefaultDrawable(usd.color)
            } else {
                usd
            }
        }
        mSize = a.getDimensionPixelSize(R.styleable.RecyclerViewBanner_rvb_indicatorSize, 0)
        mSpace =
            a.getDimensionPixelSize(R.styleable.RecyclerViewBanner_rvb_indicatorSpace, dp2px(4))
        val margin =
            a.getDimensionPixelSize(R.styleable.RecyclerViewBanner_rvb_indicatorMargin, dp2px(8))
        val g = a.getInt(R.styleable.RecyclerViewBanner_rvb_indicatorGravity, 1)
        val gravity: Int
        gravity = if (g == 0) {
            GravityCompat.START
        } else if (g == 2) {
            GravityCompat.END
        } else {
            Gravity.CENTER
        }
        a.recycle()
        recyclerView = RecyclerView(context)
        mLinearLayout = LinearLayout(context)
        PagerSnapHelper().attachToRecyclerView(
            recyclerView
        )
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = RecyclerAdapter()
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val next =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition()
                if (next != currentIndex) {
                    currentIndex = next
                    if (isShowIndicator && isTouched) {
                        isTouched = false
                        switchIndicator()
                    }
                }
            }
        })
        mLinearLayout.orientation = LinearLayout.HORIZONTAL
        mLinearLayout.gravity = Gravity.CENTER
        val vpLayoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            dp2px(260)
        )
        val linearLayoutParams = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        linearLayoutParams.gravity = Gravity.BOTTOM or gravity
        linearLayoutParams.setMargins(margin, margin, margin, margin + CommonUtil.dp2px(60f))
        addView(recyclerView, vpLayoutParams)
        addView(mLinearLayout, linearLayoutParams)
    }

    /**
     * 默认指示器是一系列直径为6dp的小圆点
     */
    private fun generateDefaultDrawable(color: Int): GradientDrawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.setSize(dp2px(6), dp2px(6))
        gradientDrawable.cornerRadius = dp2px(6).toFloat()
        gradientDrawable.setColor(color)
        return gradientDrawable
    }

    /**
     * 设置是否显示指示器导航点
     *
     * @param show 显示
     */
    fun isShowIndicator(show: Boolean) {
        isShowIndicator = show
    }

    /**
     * 设置轮播间隔时间
     *
     * @param millisecond 时间毫秒
     */
    fun setIndicatorInterval(millisecond: Int) {
        mInterval = millisecond
    }

    /**
     * 设置是否自动播放（上锁）
     *
     * @param playing 开始播放
     */
    @Synchronized
    private fun setPlaying(playing: Boolean) {
        if (isAutoPlaying) {
            if (!isPlaying && playing && adapter != null && adapter.itemCount > 2) {
                handler.postDelayed(playTask, mInterval.toLong())
                isPlaying = true
            } else if (isPlaying && !playing) {
                handler.removeCallbacksAndMessages(null)
                isPlaying = false
            }
        }
    }

    /**
     * 设置是否禁止滚动播放
     *
     * @param isAutoPlaying true  是自动滚动播放,false 是禁止自动滚动
     */
    fun setRvAutoPlaying(isAutoPlaying: Boolean) {
        this.isAutoPlaying = isAutoPlaying
    }

    /**
     * 设置轮播数据集
     *
     * @param data Banner对象列表
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setRvBannerData(data: List<NewsDTO>?) {
        initAd()
        setPlaying(false)
        mData.clear()
        if (data != null) {
            mData.addAll(data)
        }
        if (mData.size > 1) {
            adapter!!.notifyDataSetChanged()
            currentIndex = Int.MAX_VALUE / 2 - Int.MAX_VALUE / 2 % mData.size
            // 将起始点设为最靠近的 MAX_VALUE/2 的，且为mData.size()整数倍的位置
            recyclerView.scrollToPosition(currentIndex)
            if (isShowIndicator) {
                createIndicators()
            }
            setPlaying(true)
        } else {
            currentIndex = 0
            adapter!!.notifyDataSetChanged()
        }
    }

    private fun initAd() {
        if (mrecBanner == null) {
            mrecBanner = MRECBanner(context)
            mrecBanner?.scenesName =
                if (from == fromSearch) AdName.search_mrec else AdName.weather_mrec
            mrecBanner?.bindLifecycle(context)
            mrecBanner?.preLoad()
        }
    }

    /**
     * 指示器整体由数据列表容量数量的AppCompatImageView均匀分布在一个横向的LinearLayout中构成
     * 使用AppCompatImageView的好处是在Fragment中也使用Compat相关属性
     */
    private fun createIndicators() {
        mLinearLayout!!.removeAllViews()
        for (i in 0 until mData.size + 1) {
            val img = AppCompatImageView(context)
            val lp = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            lp.leftMargin = mSpace / 2
            lp.rightMargin = mSpace / 2
            if (mSize >= dp2px(4)) { // 设置了indicatorSize属性
                lp.height = mSize
                lp.width = lp.height
            } else {
                // 如果设置的resource.xml没有明确的宽高，默认最小2dp，否则太小看不清
                img.minimumWidth = dp2px(2)
                img.minimumHeight = dp2px(2)
            }
            img.setImageDrawable(if (i == 0) mSelectedDrawable else mUnselectedDrawable)
            mLinearLayout.addView(img, lp)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        //手动触摸的时候，停止自动播放，根据手势变换
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x.toInt()
                startY = ev.y.toInt()
                parent.requestDisallowInterceptTouchEvent(true)
            }

            MotionEvent.ACTION_MOVE -> {
                val moveX = ev.x.toInt()
                val moveY = ev.y.toInt()
                val disX = moveX - startX
                val disY = moveY - startY
                val hasMoved = 2 * Math.abs(disX) > Math.abs(disY)
                parent.requestDisallowInterceptTouchEvent(hasMoved)
                if (hasMoved) {
                    setPlaying(false)
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> if (!isPlaying) {
                isTouched = true
                setPlaying(true)
            }

            else -> {}
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setPlaying(true)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        setPlaying(false)
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        if (visibility == GONE || visibility == INVISIBLE) {
            // 停止轮播
            setPlaying(false)
        } else if (visibility == VISIBLE) {
            // 开始轮播
            setPlaying(true)
        }
        super.onWindowVisibilityChanged(visibility)
    }

    /**
     * RecyclerView适配器
     */
    private inner class RecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemViewType(position: Int): Int {
            return if ((position % (mData.size + 1) == 4) && mrecBanner?.isReady() == true) return AD_TYPE else 0
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val img = ItemWeatherNewBinding.inflate(LayoutInflater.from(parent.context)).root
            val params = RecyclerView.LayoutParams(
                CommonUtil.getScreenWidth() - CommonUtil.dp2px(20f),
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            img.layoutParams = params
            img.id = R.id.rvb_banner_image_view_id
            when (viewType) {
                AD_TYPE -> {
                    if (null != mrecBanner?.parent) {
                        (mrecBanner?.parent as ViewGroup).removeAllViews()
                    }

                    val fl = FrameLayout(parent.context)
                    val params2 = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    fl.layoutParams = params2
                    fl.id = R.id.rvb_banner_fl_view_id
                    fl.addView(mrecBanner)
                    return object : RecyclerView.ViewHolder(fl) {}
                }

                else -> {

                }
            }

            return object : RecyclerView.ViewHolder(img) {}
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (getItemViewType(position)) {
                AD_TYPE -> {
                    mrecBanner?.loadAd()
                }

                else -> {
                    switchBanner(
                        position % mData.size,
                        holder.itemView
                    )
                }
            }
        }

        private fun switchBanner(position: Int, bannerView: View) {
            val viewBinding = ItemWeatherNewBinding.bind(bannerView)

            mData[if (position >= mData.size) Random().nextInt(mData.size) else position].image_urls?.get(
                0
            )?.let { it1 ->
                GlideUtil.load(
                    viewBinding.iconIv,
                    it1,
                    placeholder = R.drawable.ic_news_ph
                )
            }
            viewBinding.tvTitle.text =
                HtmlCompat.fromHtml(
                    mData[if (position >= mData.size) Random().nextInt(mData.size) else position].title!!,
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                ).toString()
            viewBinding.timeTv.text = mData.get(
                if (position >= mData.size) Random().nextInt(mData.size) else position
            ).publishDate
            viewBinding.root.setOnClickListener {
                if (onRvBannerClickListener != null) {
                    onRvBannerClickListener!!.onClick(
                        mData[if (position >= mData.size) Random().nextInt(
                            mData.size
                        ) else position]
                    )
                }
            }

            if (from == fromSearch) {
                viewBinding.tvTitle.setTextColor(Color.WHITE)
                viewBinding.iconCv.radius = CommonUtil.dp2px(15f).toFloat()
            }
        }

        override fun getItemCount(): Int {
            return if (mData.size < 2) mData.size else Int.MAX_VALUE
        }
    }

    private inner class PagerSnapHelper : LinearSnapHelper() {
        override fun findTargetSnapPosition(
            layoutManager: RecyclerView.LayoutManager,
            velocityX: Int,
            velocityY: Int
        ): Int {
            var targetPos = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
            val currentView = findSnapView(layoutManager)
            if (targetPos != RecyclerView.NO_POSITION && currentView != null) {
                var currentPos = layoutManager.getPosition(currentView)
                val first = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                val last = layoutManager.findLastVisibleItemPosition()
                currentPos =
                    if (targetPos < currentPos) last else if (targetPos > currentPos) first else currentPos
                targetPos =
                    if (targetPos < currentPos) currentPos - 1 else if (targetPos > currentPos) currentPos + 1 else currentPos
            }
            return targetPos
        }
    }

    /**
     * 改变导航的指示点
     */
    private fun switchIndicator() {
        if (mLinearLayout != null && mLinearLayout.childCount > 0) {
            for (i in 0 until mLinearLayout.childCount) {
                (mLinearLayout.getChildAt(i) as AppCompatImageView).setImageDrawable(
                    if (i == currentIndex % (mData.size + 1)) mSelectedDrawable else mUnselectedDrawable
                )
            }
        }
        if (currentIndex % (mData.size + 1) == 4 && (mrecBanner?.isReady() == true)) {
            mLinearLayout?.gone()
        } else {
            mLinearLayout?.visible()
        }
    }

    private fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }

    interface OnRvBannerClickListener {
        fun onClick(date: NewsDTO)
    }

    fun setOnRvBannerClickListener(onRvBannerClickListener: OnRvBannerClickListener?) {
        this.onRvBannerClickListener = onRvBannerClickListener
    }

    companion object {
        private const val DEFAULT_SELECTED_COLOR = -0x1
        private const val DEFAULT_UNSELECTED_COLOR = 0x50ffffff

        const val AD_TYPE = 1

        const val fromWeather = 1
        const val fromSearch = 2
    }
}