package com.lambda.common.ad.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.lambda.adlib.LambdaAd
import com.lambda.adlib.LambdaAdAdapter
import com.lambda.adlib.adapter.LAdMultipleAdapter
import com.lambda.common.ad.AdUtil

class MRECBanner @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs), LifecycleEventObserver {

    val TAG = "MRECBanner"

    var scenesName: String = ""

    private var mMRECBanner: LAdMultipleAdapter? = null

    fun bindLifecycle(context: Context) {
        (context as? FragmentActivity)?.lifecycle?.addObserver(this)
    }

    private val listen = object : LambdaAdAdapter.OnAdapterClose<LAdMultipleAdapter>() {

        override fun onLoad(adapter: LAdMultipleAdapter, status: Int) {
            super.onLoad(adapter, status)
            if (status == LambdaAd.AD_FILL) {
                mMRECBanner?.showBanner(this@MRECBanner, isLoadShow = false)
            }
        }

        override fun onClose(adapter: LAdMultipleAdapter, status: Int) {
            super.onClose(adapter, status)
            if (status == LambdaAd.AD_SHOWING) {

            }
        }
    }

    fun loadAd() {
        if (mMRECBanner == null) {
            mMRECBanner = AdUtil.getADMrecAdapter(scenesName)
        }
        mMRECBanner?.onAdapterClose = listen

        if (mMRECBanner?.isReady() == true) {
            mMRECBanner?.showBanner(this@MRECBanner, isLoadShow = false)
        } else {
            mMRECBanner?.loadBanner(false)
        }
    }

    // 以下两个方法在特殊场景使用

    fun preLoadAd() {
        if (mMRECBanner == null) {
            mMRECBanner = AdUtil.getADMrecAdapter(scenesName)
        }
        if (mMRECBanner?.isReady() != true) {
            mMRECBanner?.loadBanner(false)
        }
    }

    fun showWithPreLoad() {
        if (mMRECBanner?.isReady() == true) {
            mMRECBanner?.showBanner(this@MRECBanner, isLoadShow = false)
        } else {
            // 二次展示就需要load
            mMRECBanner?.onAdapterClose = listen
            mMRECBanner?.loadBanner(false)
        }
    }

    fun isReady(): Boolean {
        return mMRECBanner?.isReady() ?: false
    }

    fun destroy() {
        mMRECBanner?.destroy()
        mMRECBanner = null
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> {
                destroy()
            }

            else -> {}
        }
    }
}