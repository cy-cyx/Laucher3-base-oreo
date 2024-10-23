package com.theme.lambda.launcher.ui.iap

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.android.launcher3.R
import com.android.launcher3.databinding.ActivityVipBinding
import com.theme.lambda.launcher.ad.AdName
import com.theme.lambda.launcher.ad.AdUtil
import com.theme.lambda.launcher.base.BaseActivity
import com.theme.lambda.launcher.statistics.EventName
import com.theme.lambda.launcher.statistics.EventUtil
import com.theme.lambda.launcher.utils.StatusBarUtil
import com.theme.lambda.launcher.vip.ProductIds
import com.theme.lambda.launcher.vip.VipManager
import com.theme.lambda.launcher.widget.dialog.LoadingDialog

class VipActivity : BaseActivity<ActivityVipBinding>() {

    companion object {

        final val FromHomeIcon = "home_icon"
        final val FromHomeBanner = "home_banner"
        final val FromPreviewDownload = "preview_download"

        val keyFrom = "keyFrom"

        fun start(context: Context, from: String) {
            if (VipManager.isVip.value == true) return
            context.startActivity(Intent(context, VipActivity::class.java).apply {
                putExtra(keyFrom, from)
            })
            EventUtil.logEvent(EventName.iapEntryClick, Bundle().apply {
                putString("placement", from)
            })
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityVipBinding {
        return ActivityVipBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<VipViewModel>()


    val loadDialog by lazy {
        LoadingDialog(this).apply {
            setCancelable(false)
        }
    }

    var from = VipActivity.FromHomeIcon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarDarkMode(this.window)

        from = intent.getStringExtra(keyFrom) ?: FromHomeIcon

        viewBinding.monthFl.setOnClickListener {
            viewModel.curSelectProduct.value = ProductIds.Monthly
            EventUtil.logEvent(EventName.iapPageClick, Bundle().apply {
                putString("from", from)
                putInt("pos", 1)
            })
        }

        viewBinding.yearFl.setOnClickListener {
            viewModel.curSelectProduct.value = ProductIds.Yearly
            EventUtil.logEvent(EventName.iapPageClick, Bundle().apply {
                putString("from", from)
                putInt("pos", 1)
            })
        }

        viewBinding.continueTv.setOnClickListener {
            viewModel.purchase(this)
            EventUtil.logEvent(EventName.iapPageClick, Bundle().apply {
                putString("from", from)
                putInt("pos", 2)
            })
        }

        viewBinding.backIv.setOnClickListener {
            finish()
            AdUtil.showAd(AdName.iap_close)
            EventUtil.logEvent(EventName.iapPageClick, Bundle().apply {
                putString("from", from)
                putInt("pos", 0)
            })
        }

        viewModel.curSelectProduct.observe(this, Observer {
            if (it == ProductIds.Monthly) {
                viewBinding.monthFl.setBackgroundResource(R.drawable.bg_vip_select)
                viewBinding.yearFl.setBackgroundResource(R.drawable.ic_vip_super_sale_bn)
                viewBinding.monthSelectIv.setImageResource(R.drawable.ic_bn_select)
                viewBinding.yearSelectIv.setImageResource(R.drawable.ic_bn_no_select)

            } else {
                viewBinding.monthFl.setBackgroundResource(R.drawable.bg_vip_no_select)
                viewBinding.yearFl.setBackgroundResource(R.drawable.ic_vip_super_sale_bn_sel)
                viewBinding.monthSelectIv.setImageResource(R.drawable.ic_bn_no_select)
                viewBinding.yearSelectIv.setImageResource(R.drawable.ic_bn_select)
            }
        })


        viewModel.loadDialogLiveData.observe(this, Observer {
            if (it) {
                loadDialog.show()
            } else {
                loadDialog.dismiss()
            }
        })

        viewModel.monthlyPriceLiveData.observe(this, Observer {
            viewBinding.mouthTv.text = it

        })
        viewModel.yearlyPriceLiveData.observe(this, Observer {
            viewBinding.yearTv.text = it
        })

        viewModel.initBilling()

        EventUtil.logEvent(EventName.iapPageView, Bundle().apply {
            putString("from", from)
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        AdUtil.showAd(AdName.iap_close)
    }
}