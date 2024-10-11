package com.theme.lambda.launcher.ad.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.airbnb.lottie.LottieAnimationView
import com.android.launcher3.R
import com.android.launcher3.databinding.LayoutAdFixBinding
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.google.android.gms.ads.nativead.NativeAdView
import com.lambda.adlib.LambdaAd
import com.lambda.adlib.LambdaAdAdapter
import com.lambda.adlib.NativeViewType
import com.lambda.adlib.adapter.LAdMultipleAdapter
import com.theme.lambda.launcher.ad.AdUtil
import com.theme.lambda.launcher.utils.visible

class ADNativeSmallView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs), LifecycleEventObserver {

    private var nativeView: MaxNativeAdView? = null
    private var nativeView1: NativeAdView? = null
    private var nativeViewPangle: View? = null

    private var placement: String? = null

    // 展示完成
    private var show = false

    // 用于未加载好，如果用于列表，出广告但是已经被回收会报view no attach错误
    private var isAttached = true

    private var adapter: LAdMultipleAdapter? = null

    private val lottie = LottieAnimationView(context)

    init {
        (context as? FragmentActivity)?.lifecycle?.addObserver(this)
        addView(lottie, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        lottie.loop(true)
        lottie.scaleType = ImageView.ScaleType.FIT_XY
        lottie.setAnimation(R.raw.native_middle_loading)
    }

    // 修复泄露，用这层UI去断context泄露
    private var adLayout =
        LayoutAdFixBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        ).root

    private var adListen: AdLambdaListen? = null

    public fun loadAd(p: String) {
        placement = p

        // 加载动画
        lottie.visible()
        lottie.playAnimation()

        // 这个写法解决泄露
        adListen = AdLambdaListen().apply {
            listen = object : LambdaAdAdapter.OnAdapterClose<LAdMultipleAdapter>() {
                override fun onClose(adapter: LAdMultipleAdapter, status: Int) {
                    super.onClose(adapter, status)
                }

                override fun onLoad(adapter: LAdMultipleAdapter, status: Int) {
                    super.onLoad(adapter, status)
                    if (status == LambdaAd.AD_FILL && AdUtil.isReady(placement!!)) {
                        if (isAttached && !show) {
                            showAd()
                            AdUtil.removeNativeAdapterClose(placement!!, adListen!!)
                        }
                    }
                }
            }
        }

        nativeView =
            AdUtil.populateNativeAdViewMax1(
                context,
                R.layout.layout_native_ad_max_1
            )
        nativeView1 = AdUtil.populateNativeAdViewAdmob1(
            context,
            R.layout.layout_native_ad_admob_1
        )
        nativeViewPangle =
            LayoutInflater.from(context).inflate(R.layout.layout_native_ad_pangle_1, null)
        val tempAdapter = AdUtil.getADAdapter(p)
        adapter = tempAdapter
        if (tempAdapter?.isReady() == true) {
            showAd()
        } else {
            AdUtil.addNativeAdapterClose(placement!!, adListen!!)
        }
    }

    fun onViewAttachedToWindow() {
        isAttached = true
        // 每次添加到屏幕都尝试刷新一下
        showLazyOrUpDataAD()
        if (!show) lottie.playAnimation()
    }

    fun onViewDetachedFromWindow() {
        isAttached = false
        lottie.pauseAnimation()
    }

    fun showLazyOrUpDataAD() {
        if (adapter?.isReady() == true) {
            showAd()
        }
    }

    private var lastShowAdTimeStamp = -1L

    private fun showAd() {
        if (System.currentTimeMillis() - lastShowAdTimeStamp < 10000) return
        adLayout.removeAllViews()

        try {
            adapter?.showNative(adLayout, HashMap<NativeViewType, View>().apply {
                nativeView?.let {
                    put(NativeViewType.MAX, it)
                }
                nativeView1?.let {
                    put(NativeViewType.ADMOB, it)
                }
                nativeViewPangle?.let {
                    put(NativeViewType.PANGLE, it)
                }
            }, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        lottie.pauseAnimation()
        show = true
        lastShowAdTimeStamp = System.currentTimeMillis()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> {
                removeView(adLayout)
                AdUtil.removeNativeAdapterClose(placement!!, adListen!!)
                adListen?.listen = null
            }

            else -> {}
        }
    }
}