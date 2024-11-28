package com.lambda.common.ad.view

import com.lambda.adlib.LambdaAdAdapter
import com.lambda.adlib.adapter.LAdMultipleAdapter

// 这个类是为了解决匿名内部类的泄露
class AdLambdaListen : LambdaAdAdapter.OnAdapterClose<LAdMultipleAdapter>() {

    var listen: LambdaAdAdapter.OnAdapterClose<LAdMultipleAdapter>? = null

    override fun onClose(adapter: LAdMultipleAdapter, status: Int) {
        super.onClose(adapter, status)
        listen?.onClose(adapter, status)
    }

    override fun onLoad(adapter: LAdMultipleAdapter, status: Int) {
        super.onLoad(adapter, status)
        listen?.onLoad(adapter, status)
    }
}