package com.theme.lambda.launcher.ad

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.android.launcher3.BuildConfig
import com.android.launcher3.R
import com.android.launcher3.databinding.LayoutNativeAdAdmob1Binding
import com.android.launcher3.databinding.LayoutNativeAdAdmob2Binding
import com.android.launcher3.databinding.LayoutNativeAdMax1Binding
import com.android.launcher3.databinding.LayoutNativeAdMax2Binding
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import com.google.android.gms.ads.nativead.NativeAdView
import com.lambda.adlib.LambdaAd
import com.lambda.adlib.LambdaAdAdapter
import com.lambda.adlib.LambdaAdSdk
import com.lambda.adlib.adapter.LAdMultipleAdapter
import com.lambda.common.utils.utilcode.util.ActivityUtils
import com.theme.lambda.launcher.Constants
import com.theme.lambda.launcher.statistics.ADEventName
import com.theme.lambda.launcher.statistics.EventUtil
import com.theme.lambda.launcher.statistics.FirebaseAnalyticsUtil
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.LogUtil
import com.theme.lambda.launcher.utils.SpKey
import com.theme.lambda.launcher.utils.getMMKVFloat
import com.theme.lambda.launcher.utils.putMMKVFloat
import com.theme.lambda.launcher.vip.VipManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
object AdUtil : Application.ActivityLifecycleCallbacks {

    val TAG = "AdUtil"
    private var initSuccess = false

    // 专门留一个activity用于泄露使用
    private var adActivity: Activity? = null

    fun getWapActivity(): Activity? = adActivity ?: ActivityUtils.getTopActivity()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val lAdMultipleAdapters = mutableMapOf<String, LAdMultipleAdapter?>()

    // 这种写法 需求所需 可能存在泄露 但是不能使用弱引用
    private var lastCallback: IAdCallBack? = null

    // 由于adapter是单个，回调会被顶，故用集合
    private val nativeAdapterCloseMap =
        HashMap<String, ArrayList<LambdaAdAdapter.OnAdapterClose<LAdMultipleAdapter>>>()

    private var lastShowAdMillis = 0L

    private const val adSourceMAXAdMob = "Google AdMob" // max聚合里admob的ad_source
    private const val adSourceMAXFacebook = "Facebook" // max聚合里facebook的ad_source
    private const val adSourceAdMobAdMob = "AdMob Network" // admob聚合里admob的ad_source
    private const val adSourceTestAdMob = "Reservation campaign" // admob测试广告
    val AdmobAdSources = listOf(adSourceAdMobAdMob, adSourceMAXAdMob, adSourceMAXFacebook, adSourceTestAdMob)

    @Synchronized
    fun addNativeAdapterClose(
        scenes: String, adapterClose: LambdaAdAdapter.OnAdapterClose<LAdMultipleAdapter>
    ) {
        val list = nativeAdapterCloseMap[scenes] ?: ArrayList()
        if (!list.contains(adapterClose)) {
            // 确保最后进的第一个设置
            list.add(0, adapterClose)
        }
        nativeAdapterCloseMap[scenes] = list
    }

    @Synchronized
    fun removeNativeAdapterClose(
        scenes: String, adapterClose: LambdaAdAdapter.OnAdapterClose<LAdMultipleAdapter>
    ) {
        val list = nativeAdapterCloseMap[scenes] ?: ArrayList()
        list.remove(adapterClose)
        nativeAdapterCloseMap[scenes] = list
    }


    fun initAd(app: Application) {
        app.registerActivityLifecycleCallbacks(this)
        LambdaAdSdk.registerLife(app)

        LambdaAdSdk.init(
            Constants.BASE_URL, Constants.SECRET_KEY, object : LambdaAd.LogAdEvent {
                override fun onLog(step: Int, logParam: LambdaAd.LogAdEvent.LogParam?, ad: Any?) {
                    LogUtil.d(TAG, "step: $step, logParam: $logParam, ad: $ad")
                    when (step) {
                        LambdaAd.LogAdEvent.LOG_LOAD -> {
                            EventUtil.logEvent(ADEventName.adLoad, Bundle().apply {
                                putString("adid", logParam?.adId ?: "")
                                putString("ad_source", logParam?.adSource ?: "")
                                putInt("ad_type", logParam?.ad_type ?: 0)
                                putString("ad_type_alias", logParam?.getAdTypeAlias() ?: "")
                                putInt("reload", logParam?.reload ?: 0)
                                putLong("start_time", System.currentTimeMillis())
                                putString("med_source", logParam?.med_source ?: "0")
                                putString("scene_alias", logParam?.name ?: "")
                            })
                        }

                        LambdaAd.LogAdEvent.LOG_FILL -> {
                            EventUtil.logEvent(ADEventName.adFill, Bundle().apply {
                                putString("adid", logParam?.adId ?: "")
                                putString("ad_source", logParam?.adSource ?: "")
                                putInt("ad_type", logParam?.ad_type ?: 0)
                                putString("ad_type_alias", logParam?.getAdTypeAlias() ?: "")
                                putInt("reload", logParam?.reload ?: 0)
                                putString("med_source", logParam?.med_source ?: "0")
                                putString("scene_alias", logParam?.name ?: "")
                                putLong("loadtime", logParam?.loadTime ?: 0L)
                                putFloat("revenue", logParam?.revenue?.toFloat() ?: 0.0f)
                                putFloat("value", logParam?.revenue?.toFloat() ?: 0.0f)
                                putString("currency", "USD")
                            })
                        }

                        LambdaAd.LogAdEvent.LOG_LOAD_FAIL -> {
                            EventUtil.logEvent(ADEventName.adLoadFailed, Bundle().apply {
                                putString("adid", logParam?.adId ?: "")
                                putString("ad_source", logParam?.adSource ?: "")
                                putInt("ad_type", logParam?.ad_type ?: 0)
                                putString("ad_type_alias", logParam?.getAdTypeAlias() ?: "")
                                putString("scene_alias", logParam?.name ?: "")
                                putInt("reload", logParam?.reload ?: 0)
                                putString("med_source", logParam?.med_source ?: "0")
                                putInt("code", logParam?.code ?: 0)
                                putString("err_msg", logParam?.errMsg ?: "")
                            })
                        }

                        LambdaAd.LogAdEvent.LOG_REQUEST -> {
                            EventUtil.logEvent(ADEventName.adRequest, Bundle().apply {
                                putString("adid", logParam?.adId ?: "")
                                putString("ad_source", logParam?.adSource ?: "")
                                putInt("ad_type", logParam?.ad_type ?: 0)
                                putString("ad_type_alias", logParam?.getAdTypeAlias() ?: "")
                                putString("scene_alias", logParam?.name ?: "")
                                putInt("code", 0)
                            })
                        }

                        LambdaAd.LogAdEvent.LOG_SHOW -> {
                            EventUtil.logEvent(ADEventName.adShow, Bundle().apply {
                                putString("adid", logParam?.adId ?: "")
                                putString("ad_source", logParam?.adSource ?: "")
                                putInt("ad_type", logParam?.ad_type ?: 0)
                                putString("ad_type_alias", logParam?.getAdTypeAlias() ?: "")
                                putString("med_source", logParam?.med_source ?: "0")
                                putInt("reload", logParam?.reload ?: 0)
                                putString("scene_alias", logParam?.name ?: "")
                                putBoolean("isVisible", logParam?.isVisible ?: false)
                            })
                        }

                        LambdaAd.LogAdEvent.LOG_SHOW_FAIL -> {
                            EventUtil.logEvent(ADEventName.adShowFailed, Bundle().apply {
                                putString("adid", logParam?.adId ?: "")
                                putString("ad_source", logParam?.adSource ?: "")
                                putInt("ad_type", logParam?.ad_type ?: 0)
                                putString("ad_type_alias", logParam?.getAdTypeAlias() ?: "")
                                putString("scene_alias", logParam?.name ?: "")
                                putInt("code", logParam?.code ?: 0)
                                putString("err_msg", logParam?.errMsg ?: "")
                            })
                        }

                        LambdaAd.LogAdEvent.LOG_CLICK -> {
                            EventUtil.logEvent(ADEventName.adClick, Bundle().apply {
                                putString("adid", logParam?.adId ?: "")
                                putString("ad_source", logParam?.adSource ?: "")
                                putInt("ad_type", logParam?.ad_type ?: 0)
                                putString("ad_type_alias", logParam?.getAdTypeAlias() ?: "")
                                putString("med_source", logParam?.med_source ?: "0")
                                putString("scene_alias", logParam?.name ?: "")
                            })
                        }

                        LambdaAd.LogAdEvent.LOG_REVENUE -> {
                            EventUtil.logEvent(ADEventName.adRevenue, Bundle().apply {
                                putString("adid", logParam?.adId ?: "")
                                putString("ad_source", logParam?.adSource ?: "")
                                putFloat("revenue", logParam?.revenue?.toFloat() ?: 0.0f)
                                putFloat("value", logParam?.revenue?.toFloat() ?: 0.0f)
                                putInt("ad_type", logParam?.ad_type ?: 0)
                                putString("ad_type_alias", logParam?.getAdTypeAlias() ?: "")
                                putString("scene_alias", logParam?.name ?: "")
                                putString("resp_id", logParam?.resp_id ?: "")
                                putLong("cache_time", logParam?.cache_time ?: 0L)
                                putString("currency", "USD")
                                putBoolean("isVisible", logParam?.isVisible ?: false)
                            })

                            // 多上传一份给firebase（单独）
                            FirebaseAnalyticsUtil.logEvent(
                                ADEventName.ad_impression,
                                Bundle().apply {
                                    putFloat("value", logParam?.revenue?.toFloat() ?: 0.0f)
                                    putString("currency", "USD")
                                    putString("adid", logParam?.adId ?: "")
                                })

                            // 累计收益上传两份firebase（单独）
                            // 用于每累计满0.01 0.02 0.03上传一次，上传后清零
                            var income001 = SpKey.cumulative_income_001.getMMKVFloat(0f)
                            logParam?.revenue?.toFloat()?.let {
                                income001 += it
                            }
                            SpKey.cumulative_income_001.putMMKVFloat(income001)

                            if (income001 > 0.01f) {
                                FirebaseAnalyticsUtil.logEvent(
                                    ADEventName.totalAdRevenue001,
                                    Bundle().apply {
                                        putFloat("value", income001)
                                        putString("currency", "USD")
                                    })
                                FirebaseAnalyticsUtil.logEvent(
                                    ADEventName.adRev001,
                                    Bundle().apply {
                                        putFloat("value", income001)
                                        putString("currency", "USD")
                                    })
                                SpKey.cumulative_income_001.putMMKVFloat(0f)
                            }
                        }

                        LambdaAd.LogAdEvent.LOG_INIT_SDK_SUCCESS -> {
                            initSuccess = true
                            // 广告预加载
                            getWapActivity()?.let {
                                loadAd(it)
                            }
                        }
                    }
                }
            }, BuildConfig.isDebug
        )
            // 存在ANR，又必须在主线程 -_-
            .initRemoteConfig(
                when (CommonUtil.getRegion()) {
                    "RU" -> {
                        R.xml.remote_config_ru
                    }

                    else -> {
                        R.xml.remote_config_defaults
                    }
                },
                listOf("AdConfig"),
                "AdConfig"
            )
    }

    private var isInit = false

    fun loadAd(activity: Activity) {
        if (!initSuccess) {
            return
        }

        // 防止重复调用
        if (isInit) return
        isInit = true

        // 全屏类
        for (i in listOf(
            AdName.splash,
            AdName.interleaving,
            AdName.unlock,
            AdName.iap_close
        )) {
            if (lAdMultipleAdapters[i] != null) {
                continue
            }
            LAdMultipleAdapter(activity,
                i,
                object : LambdaAdAdapter.OnAdapterClose<LAdMultipleAdapter>() {
                    override fun onClose(adapter: LAdMultipleAdapter, status: Int) {
                        super.onClose(adapter, status)
                        LogUtil.d(TAG, "adapter: ${adapter}, status: $status")
                        if (status != LambdaAd.AD_SHOWING) {
                            lastCallback?.onAdClose(status)
                            lastCallback = null
                        }
                    }
                }).apply {
                lAdMultipleAdapters[i] = this
                loadInterstitial(true)
            }
        }
        // native
        for (i in listOf(
            AdName.download_nat,
            AdName.theme_new_nat
        )) {
            if (lAdMultipleAdapters[i] != null) {
                continue
            }
            LAdMultipleAdapter(activity,
                i,
                object : LambdaAdAdapter.OnAdapterClose<LAdMultipleAdapter>() {}).apply {
                lAdMultipleAdapters[i] = this
                loadNative()
                onAdapterClose = object : LambdaAdAdapter.OnAdapterClose<LAdMultipleAdapter>() {
                    override fun onLoad(adapter: LAdMultipleAdapter, status: Int) {
                        super.onLoad(adapter, status)
                        // 如果不切到主线程回调 可能出现异步问题
                        scope.launch {
                            nativeAdapterCloseMap[i]?.let {
                                val temp = ArrayList(it)
                                temp.forEach { close ->
                                    close.onLoad(adapter, status)
                                }
                            }
                        }
                    }

                    override fun onClose(adapter: LAdMultipleAdapter, status: Int) {
                        super.onClose(adapter, status)
                        nativeAdapterCloseMap[i]?.let {
                            val temp = ArrayList(it)
                            temp.forEach { close ->
                                close.onClose(adapter, status)
                            }
                        }
                    }
                }
            }
        }
    }

    fun getADAdapter(scenes: String): LAdMultipleAdapter? {
        val temp = lAdMultipleAdapters[scenes]
        temp?.name = scenes
        return temp
    }

    fun isReady(scenes: String): Boolean {
        return getADAdapter(scenes)?.isReady() ?: false
    }

    fun isReady(scenes: String, network: String, format: Int): Boolean {
        return getADAdapter(scenes)?.isReady(network, format) ?: false
    }

    fun showAd(scenes: String, callback: IAdCallBack? = null) {
        if (VipManager.isVip.value == true) {
            callback?.onAdClose(LambdaAd.AD_CLOSE)
            return
        }

        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastShowAdMillis < 500L) {
            return
        }
        lastShowAdMillis = currentTimeMillis

        lastCallback = null

        if (!isReady(scenes)) {
            callback?.onNoReady()
            return
        }
        val multiAdCallback = callback
        if (multiAdCallback != null) {
            lastCallback = multiAdCallback
        }
        getADAdapter(scenes)?.showInterstitial()
        return
    }


    fun populateNativeAdViewMax1(context: Context, layoutId: Int): MaxNativeAdView {
        val binding = LayoutNativeAdMax1Binding.bind(
            LayoutInflater.from(context).inflate(layoutId, null)
        )
        return MaxNativeAdView(
            MaxNativeAdViewBinder.Builder(binding.root).setTitleTextViewId(binding.titleTv.id)
                .setBodyTextViewId(binding.desTv.id).setIconImageViewId(binding.iconIv.id)
                .setMediaContentViewGroupId(binding.logoFl.id)
                .setCallToActionButtonId(binding.buttonBn.id)
//                .setAdvertiserTextViewId(binding.advertiserTextView.id)
//                .setOptionsContentViewGroupId(binding.adOptionsView.id)
                .build(), context
        )
    }

    fun populateNativeAdViewAdmob1(context: Context, layoutId: Int): NativeAdView {
        val binding = LayoutNativeAdAdmob1Binding.bind(
            LayoutInflater.from(context).inflate(layoutId, null)
        )
        val nativeView = binding.root as NativeAdView

        nativeView.mediaView = binding.logoFl
        nativeView.iconView = binding.iconIv
        nativeView.headlineView = binding.titleTv
        nativeView.bodyView = binding.desTv
        nativeView.callToActionView = binding.buttonBn

        return nativeView
    }

    fun populateNativeAdViewMax2(context: Context, layoutId: Int): MaxNativeAdView {
        val binding = LayoutNativeAdMax2Binding.bind(
            LayoutInflater.from(context).inflate(layoutId, null)
        )
        return MaxNativeAdView(
            MaxNativeAdViewBinder.Builder(binding.root).setTitleTextViewId(binding.titleTv.id)
                .setBodyTextViewId(binding.desTv.id).setIconImageViewId(binding.iconIv.id)
                .setMediaContentViewGroupId(binding.logoFl.id)
                .setCallToActionButtonId(binding.buttonBn.id)
//                .setAdvertiserTextViewId(binding.advertiserTextView.id)
//                .setOptionsContentViewGroupId(binding.adOptionsView.id)
                .build(), context
        )
    }

    fun populateNativeAdViewAdmob2(context: Context, layoutId: Int): NativeAdView {
        val binding = LayoutNativeAdAdmob2Binding.bind(
            LayoutInflater.from(context).inflate(layoutId, null)
        )
        val nativeView = binding.root as NativeAdView

        nativeView.mediaView = binding.logoFl
        nativeView.iconView = binding.iconIv
        nativeView.headlineView = binding.titleTv
        nativeView.bodyView = binding.desTv
        nativeView.callToActionView = binding.buttonBn

        return nativeView
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (adActivity == null) {
            adActivity = activity
        }
        loadAd(activity)
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }
}