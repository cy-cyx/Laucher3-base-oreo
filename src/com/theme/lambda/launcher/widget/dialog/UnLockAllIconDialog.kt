package com.theme.lambda.launcher.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.android.launcher3.R
import com.android.launcher3.databinding.DialogUnlockAllIconBinding
import com.lambda.common.ad.AdName
import com.lambda.common.ad.AdUtil
import com.lambda.common.ad.IAdCallBack
import com.lambda.common.statistics.EventName
import com.lambda.common.statistics.EventUtil
import com.theme.lambda.launcher.ui.iap.VipActivity
import com.lambda.common.utils.CommonUtil

class UnLockAllIconDialog(context: Context) : Dialog(context, R.style.Theme_translucentDialog) {

    val viewBinding = DialogUnlockAllIconBinding.inflate(LayoutInflater.from(context))

    var clickVipListen: (() -> Unit)? = null

    var unLockAllListen: (() -> Unit)? = null

    val needWatchAdSum = 3
    var hasWatchAdTime = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(viewBinding.root)

        val params = window?.attributes
        params?.width = CommonUtil.dp2px(320f)
        window?.attributes = params

        setCanceledOnTouchOutside(false)
        setCancelable(false)

        viewBinding.clickVipTv.setOnClickListener {
            clickVipListen?.invoke()
        }

        viewBinding.closeIv.setOnClickListener {
            dismiss()
        }
        viewBinding.clickAdTv.setOnClickListener {
            if (AdUtil.isReady(AdName.icon_unlock)) {
                AdUtil.showAd(AdName.icon_unlock, object : IAdCallBack {
                    override fun onNoReady() {

                    }

                    override fun onAdClose(status: Int) {
                        hasWatchAdTime++
                        upDataBnText()
                        if (hasWatchAdTime >= needWatchAdSum) {
                            unLockAllListen?.invoke()
                        }
                    }
                })
            } else {
                Toast.makeText(context, R.string.ad_no_fill_tip, Toast.LENGTH_SHORT).show()
            }

        }

        upDataBnText()

        EventUtil.logEvent(EventName.iapEntryView, Bundle().apply {
            putString("placement", VipActivity.FromGetAllInTheme)
        })
    }

    private fun upDataBnText() {
        viewBinding.clickAdTv.setText(
            context.getString(
                R.string.watch_ad_to_unlock_all,
                hasWatchAdTime
            )
        )
    }
}