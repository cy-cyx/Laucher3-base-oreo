package com.theme.lambda.launcher.vip

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails
import com.lambda.common.billing.Billing
import com.lambda.common.billing.core.InitParam
import com.lambda.common.billing.data.FreeAdUntilRes
import com.lambda.common.http.AppException
import com.lambda.common.http.Callback
import com.theme.lambda.launcher.Constants
import com.theme.lambda.launcher.utils.LogUtil
import com.theme.lambda.launcher.utils.SpKey
import com.theme.lambda.launcher.utils.getMMKVBool
import com.theme.lambda.launcher.utils.putMMKVBool
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object VipManager {

    val TAG = "VipManager"

    val isVip = MutableLiveData<Boolean>(false)

    var productDetails: List<ProductDetails?>? = null

    fun init() {
        Billing.init(InitParam.Builder(Constants.BASE_URL, Constants.SECRET_KEY).build())
        isVip.value = SpKey.isVip.getMMKVBool()
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
        Billing.getFreeAdUntil(object : Callback<FreeAdUntilRes?> {
            override fun onFailed(e: AppException) {
                LogUtil.e(TAG, "upDataFreeAdUntil onFailed $e")
            }

            override fun onRequest() {

            }

            override fun onSuccess(t: FreeAdUntilRes?) {
                val freeAdUntil = t?.freeAdUntil ?: 0
                isVip.postValue(freeAdUntil > System.currentTimeMillis())
                SpKey.isVip.putMMKVBool(freeAdUntil > System.currentTimeMillis())
                LogUtil.d(TAG, "upDataFreeAdUntil onSuccess freeAdUntil:$freeAdUntil")
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
        }
    }

    fun queryProducts(callback: Callback<List<ProductDetails?>?>? = null) {
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

    fun purchase(activity: FragmentActivity, product: String, callback: Callback<Void?>) {
        LogUtil.d(TAG, "purchase ------>>>")
        Billing.purchase(
            activity,
            product,
            "",
            BillingClient.ProductType.SUBS,
            "",
            object : Callback<Void?> {
                override fun onFailed(e: AppException) {
                    LogUtil.d(TAG, "purchase onFailed ${e}")
                    callback.onFailed(e)
                }

                override fun onRequest() {
                    callback.onRequest()
                }

                override fun onSuccess(t: Void?) {
                    LogUtil.d(TAG, "purchase onSuccess")
                    isVip.postValue(true)
                    SpKey.isVip.putMMKVBool(true)
                    callback.onSuccess(t)
                }
            })
    }
}