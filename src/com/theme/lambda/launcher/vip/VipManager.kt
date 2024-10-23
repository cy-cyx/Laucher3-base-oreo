package com.theme.lambda.launcher.vip

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails
import com.android.launcher3.BuildConfig
import com.lambda.common.billing.Billing
import com.lambda.common.billing.core.InitParam
import com.lambda.common.billing.data.ProductRes
import com.lambda.common.billing.data.UserAssets
import com.lambda.common.http.AppException
import com.lambda.common.http.Callback
import com.theme.lambda.launcher.Constants
import com.theme.lambda.launcher.utils.LogUtil
import com.theme.lambda.launcher.utils.SpKey
import com.theme.lambda.launcher.utils.getSpBool
import com.theme.lambda.launcher.utils.putSpBool
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object VipManager {

    val TAG = "VipManager"

    val isVip = MutableLiveData<Boolean>(false)

    var productDetails: List<ProductDetails?>? = null
    var productRes: ProductRes? = null

    fun init() {
        Billing.isDebug = BuildConfig.isDebug
        Billing.init(InitParam.Builder(Constants.BASE_URL, Constants.SECRET_KEY).build())
        isVip.value = SpKey.isVip.getSpBool()
        // 延迟请求网络有坑，归因问题
        GlobalScope.launch {
            delay(3000)
            upDataFreeAdUntil()
        }
    }

    private var lastUpDataFreeAdUntilTimeStamp = 0L

    fun upDataFreeAdUntil() {
        if (System.currentTimeMillis() - lastUpDataFreeAdUntilTimeStamp < 5 * 1000 * 60) {
            return
        }
        lastUpDataFreeAdUntilTimeStamp = System.currentTimeMillis()

        LogUtil.d(TAG, "getFreeAdUntil ------>>>")
        Billing.getFreeAdUntil(object : Callback<com.lambda.common.billing.data.UserAssets?> {
            override fun onFailed(e: AppException) {
                LogUtil.e(TAG, "upDataFreeAdUntil onFailed $e")
            }

            override fun onRequest() {

            }

            override fun onSuccess(t: UserAssets?) {
                if (t?.assets?.isNotEmpty() == true) {
                    val freeAdUntil: Long = t.assets.maxBy { it.expireAt }.expireAt
                    isVip.postValue(freeAdUntil > System.currentTimeMillis())
                    SpKey.isVip.putSpBool(freeAdUntil > System.currentTimeMillis())
                    LogUtil.d(TAG, "upDataFreeAdUntil onSuccess freeAdUntil:$freeAdUntil")
                } else {
                    isVip.postValue(false)
                    SpKey.isVip.putSpBool(false)
                }
            }

        })
    }

    /**
     * 这种sdk的缺陷之处,可能快绑定会乱掉 先用着吧
     */
    fun bindVipActivity(activity: FragmentActivity) {
        Billing.initClient(activity)
        Billing.initBilling(activity)

        GlobalScope.launch {
            delay(1000)
            queryProducts()
            getSubscriptions()
        }
    }

    fun getSubscriptions() {
        LogUtil.d(TAG, "getSubscriptions ------>>>")
        Billing.getSubscriptions(
            mutableMapOf(),
            object : Callback<ProductRes?> {
                override fun onFailed(e: AppException) {
                    LogUtil.d(TAG, "getSubscriptions onFailed : ${e}")
                }

                override fun onRequest() {
                }

                override fun onSuccess(t: ProductRes?) {
                    productRes = t
                    LogUtil.d(TAG, "getSubscriptions onSuccess : ${t}")
                }
            })
    }

    fun queryProducts(callback: Callback<List<ProductDetails?>?>? = null) {
        getSubscriptions()
        LogUtil.d(TAG, "queryProducts ------>>>")
        Billing.queryProducts(
            arrayListOf(ProductIds.Yearly.id, ProductIds.Monthly.id),
            BillingClient.ProductType.SUBS,
            object : Callback<List<ProductDetails?>?> {
                override fun onFailed(e: AppException) {
                    callback?.onFailed(e)
                    LogUtil.d(TAG, "queryProducts onFailed:$e")
                }

                override fun onRequest() {
                    callback?.onRequest()
                }

                override fun onSuccess(t: List<ProductDetails?>?) {
                    productDetails = t
                    callback?.onSuccess(t)
                    LogUtil.d(TAG, "queryProducts onSuccess:$t")
                }

            }
        )
    }

    fun purchase(
        activity: FragmentActivity,
        product: String,
        tag: String,
        callback: Callback<Void?>
    ) {
        LogUtil.d(TAG, "purchase ------>>>")
        val subscriptions = productRes?.products?.flatMap { subscriptionType ->
            subscriptionType.platProducts
        }
        val subscription = subscriptions?.lastOrNull { platProducts ->
            platProducts.platPid == product
        }
        val extraData = subscription?.croExtraData ?: ""

        if (extraData == "") {
            callback.onFailed(AppException())
            return
        }

        Billing.purchase(
            activity,
            product,
            extraData,
            BillingClient.ProductType.SUBS,
            tag,
            object : Callback<Void?> {
                override fun onFailed(e: AppException) {
                    LogUtil.d(TAG, "purchase onFailed ${e}")

                    // 轮询失败当做成功处理
                    if (e.c == com.lambda.common.billing.core.Constants.ERROR_CODE_PENDING) {
                        LogUtil.d(TAG, "success")
                        markSubsSuccess()
                        callback.onSuccess(null)
                    } else {
                        callback.onFailed(e)
                    }
                }

                override fun onRequest() {
                    callback.onRequest()
                }

                override fun onSuccess(t: Void?) {
                    LogUtil.d(TAG, "purchase onSuccess")
                    markSubsSuccess()
                    callback.onSuccess(t)
                }
            })
    }

    fun markSubsSuccess() {
        isVip.postValue(true)
        SpKey.isVip.putSpBool(true)
    }
}