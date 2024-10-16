package com.theme.lambda.launcher.ui.iap

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
        details?.let {
            var yearlyProductDetails = it.find { it?.productId == ProductIds.Yearly.id }


            var monthlyProductDetails = it.find { it?.productId == ProductIds.Monthly.id }


        }
    }

    fun purchase(activity: VipActivity) {
        VipManager.purchase(activity, curSelectProduct.value!!, object : Callback<Void?> {
            override fun onFailed(e: AppException) {

            }

            override fun onRequest() {

            }

            override fun onSuccess(t: Void?) {

            }
        })
    }
}