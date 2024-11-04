package com.theme.lambda.launcher.ad.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.lambda.adlib.LambdaAd
import com.lambda.adlib.LambdaAdAdapter
import com.lambda.adlib.adapter.LAdMultipleAdapter

class MRECBanner @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    val TAG = "MRECBanner"

    var scenesName: String = ""

    private var mMRECBanner: LAdMultipleAdapter? = null

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
}