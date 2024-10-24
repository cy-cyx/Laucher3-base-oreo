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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AdBanner2 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs), LifecycleEventObserver {
    private var job: Job? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var isResume = true
    private var isLoadedCollapse = false
    var scenesName: String = ""
    val autoRefreshInterval: Long = 30 * 1000L

    init {
        (context as? FragmentActivity)?.lifecycle?.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                isResume = true
                showBannerAd()
            }

            Lifecycle.Event.ON_PAUSE -> {
                isResume = false
            }

            Lifecycle.Event.ON_DESTROY -> {
                job?.cancel()
                mBannerAdapter?.destroy()
            }

            else -> {}
        }
    }

    private var mBannerAdapter: LAdMultipleAdapter? = null
    private fun showBannerAd() {
        if (mBannerAdapter == null) {
            mBannerAdapter = LAdMultipleAdapter(this.context as Activity,
                scenesName,
                object : LambdaAdAdapter.OnAdapterClose<LAdMultipleAdapter>() {

                    override fun onLoad(adapter: LAdMultipleAdapter, status: Int) {
                        super.onLoad(adapter, status)
                        if (status == LambdaAd.AD_FILL) {
                            if (!isResume) return
                            adapter.showBanner(this@AdBanner2, isLoadShow = false)
                            isLoadedCollapse = true
                            showAutoShow(autoRefreshInterval)
                        } else if (status == LambdaAd.AD_LOAD_FAIL) {
                        }
                    }

                    override fun onClose(adapter: LAdMultipleAdapter, status: Int) {
                        super.onClose(adapter, status)
                        if (status == LambdaAd.AD_SHOWING) {

                        } else if (status == LambdaAd.AD_CLICK) {

                        }
                    }
                })
        }
        mBannerAdapter?.loadBanner(false, !isLoadedCollapse)
    }

    private fun showAutoShow(delay: Long) {
        job?.cancel()
        job = scope.launch {
            delay(delay)
            if (!isResume) {
                return@launch
            }
            if (mBannerAdapter?.isReady() == true) {
                showBannerAd()
            } else {
                // 如果没有准备好 5秒后再试试
                showAutoShow(5000)
            }
        }
    }
}