package com.theme.lambda.launcher.ui.iap

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.ProductDetails
import com.lambda.common.http.AppException
import com.lambda.common.http.Callback
import com.theme.lambda.launcher.base.BaseViewModel
import com.theme.lambda.launcher.vip.ProductIds
import com.theme.lambda.launcher.vip.VipManager

class VipViewModel : BaseViewModel() {

    var details: List<ProductDetails?>? = null

    var curSelectProduct = MutableLiveData(ProductIds.Monthly.id)

    var loadDialogLiveData = MutableLiveData<Boolean>()

    var monthlyPriceLiveData = MutableLiveData<String>()
    var yearlyPriceLiveData = MutableLiveData<String>()

    fun initBilling() {
        details = VipManager.productDetails
        details?.let {
            bindPrice()
        } ?: kotlin.run {
            VipManager.queryProducts(object : Callback<List<ProductDetails?>?> {
                override fun onFailed(e: AppException) {

                }

                override fun onRequest() {

                }

                override fun onSuccess(t: List<ProductDetails?>?) {
                    details = VipManager.productDetails
                    bindPrice()
                }
            })
        }
    }

    private fun bindPrice() {
        details?.let { detail ->
            val yearlyProductDetails = detail.find { it?.productId == ProductIds.Yearly.id }
            yearlyProductDetails?.let { it ->
                it.subscriptionOfferDetails?.forEach { it1 ->
                    it1.pricingPhases.pricingPhaseList.forEach { it2 ->
                        if (it2.priceAmountMicros != 0L) {
                            yearlyPriceLiveData.postValue("${it2.formattedPrice}/year")
                        }
                    }
                }
            }


            val monthlyProductDetails = detail.find { it?.productId == ProductIds.Monthly.id }
            monthlyProductDetails?.let {
                it.subscriptionOfferDetails?.forEach { it1 ->
                    it1.pricingPhases.pricingPhaseList.forEach { it2 ->
                        if (it2.priceAmountMicros != 0L) {
                            monthlyPriceLiveData.postValue("${it2.formattedPrice}/mouth")
                        }
                    }
                }
            }

        }
    }

    fun purchase(activity: VipActivity) {
        if (details == null) {
            Toast.makeText(activity, "Failed! Please try again later", Toast.LENGTH_SHORT).show()
            return
        }
        loadDialogLiveData.postValue(true)

        VipManager.purchase(activity, curSelectProduct.value!!, object : Callback<Void?> {
            override fun onFailed(e: AppException) {
                loadDialogLiveData.postValue(false)
            }

            override fun onRequest() {

            }

            override fun onSuccess(t: Void?) {
                loadDialogLiveData.postValue(false)
                activity.finish()
                Toast.makeText(activity, "Subscription Success", Toast.LENGTH_LONG).show()
            }
        })
    }
}