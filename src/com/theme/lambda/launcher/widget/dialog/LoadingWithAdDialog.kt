package com.theme.lambda.launcher.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.launcher3.R
import com.android.launcher3.databinding.DialogLoadingWithAdBinding
import com.theme.lambda.launcher.ad.AdName

class LoadingWithAdDialog(context: Context) : Dialog(context, R.style.Theme_translucentDialog) {

    val viewBinding = DialogLoadingWithAdBinding.inflate(LayoutInflater.from(context))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        val params = window?.attributes
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.MATCH_PARENT
        window?.attributes = params

        setCanceledOnTouchOutside(false)
        setCancelable(false)
    }

    private var hasLoadingAd = false

    private fun loadAd() {
        if (hasLoadingAd) {
            viewBinding.adView.showLazyOrUpDataAD()
        } else {
            hasLoadingAd = true
            viewBinding.adView.loadAd(AdName.download_nat)
        }
    }

    override fun show() {
        super.show()
        loadAd()
    }
}