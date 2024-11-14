package com.theme.lambda.launcher.ad.view

import android.app.Activity
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

class MRECBanner @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs), LifecycleEventObserver {

    val TAG = "MRECBanner"

    var scenesName: String = ""

    private var mMRECBanner: LAdMultipleAdapter? = null

    fun bindLifecycle(context: Context) {
        (context as? FragmentActivity)?.lifecycle?.addObserver(this)
    }

    fun loadAd() {
        if (mMRECBanner == null) {
            mMRECBanner = LAdMultipleAdapter(this.context as Activity,
                scenesName,
                object : LambdaAdAdapter.OnAdapterClose<LAdMultipleAdapter>() {

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
                })
        }
        mMRECBanner?.loadBanner(false)
    }

    private fun destroy() {
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