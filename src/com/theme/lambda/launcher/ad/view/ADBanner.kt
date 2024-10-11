package com.theme.lambda.launcher.ad.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.airbnb.lottie.LottieAnimationView
import com.android.launcher3.R
import com.lambda.adlib.LambdaAd
import com.lambda.adlib.LambdaAdAdapter
import com.lambda.adlib.adapter.LAdMultipleAdapter
import com.theme.lambda.launcher.utils.gone
import com.theme.lambda.launcher.utils.visible

// 支持折叠banner且仅切换时刷新（不然折叠banner一直挡住使用）
class ADBanner @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs), LifecycleEventObserver {

    private val lottie = LottieAnimationView(context)

    init {
        (context as? FragmentActivity)?.lifecycle?.addObserver(this)

        addView(lottie)
        lottie.loop(true)
        lottie.scaleType = ImageView.ScaleType.FIT_XY
        lottie.setAnimation(R.raw.banner_loading)
    }

    var scenesName = ""

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                showBannerAd()
            }

            Lifecycle.Event.ON_DESTROY -> {
                mBannerAdapter?.destroy()
            }

            else -> {}
        }
    }

    private var lastShowAdTimeStamp = -1L

    private var mBannerAdapter: LAdMultipleAdapter? = null
    private fun showBannerAd() {
        if (mBannerAdapter == null) {
            mBannerAdapter = LAdMultipleAdapter(this.context as Activity,
                scenesName,
                object : LambdaAdAdapter.OnAdapterClose<LAdMultipleAdapter>() {

                    override fun onLoad(adapter: LAdMultipleAdapter, status: Int) {
                        super.onLoad(adapter, status)
                        if (status == LambdaAd.AD_FILL) {
                            // sdk里面可能有个源同时更新，刷新限制避免刷新
                            if (System.currentTimeMillis() - lastShowAdTimeStamp < 5000) return
                            adapter.showBanner(this@ADBanner, isLoadShow = false)
                            lastShowAdTimeStamp = System.currentTimeMillis()

                        } else if (status == LambdaAd.AD_LOAD_FAIL) {
                            lottie.visible()
                        }
                    }

                    override fun onClose(adapter: LAdMultipleAdapter, status: Int) {
                        super.onClose(adapter, status)
                        if (status == LambdaAd.AD_SHOWING) {
                            lottie.gone()
                        }
                    }
                })
        }
        mBannerAdapter?.loadBanner(false, tryCollapse = true)
    }
}