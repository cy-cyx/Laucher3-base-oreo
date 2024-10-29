package com.theme.lambda.launcher.ad

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

import com.theme.lambda.launcher.ad.AdUtil.showAd

class OpenAppAdActivity : FragmentActivity() {

    companion object {
        var runnable: Runnable? = null
        var adId = ""

        fun start(context: Context, id: String, r: Runnable) {
            adId = id
            runnable = r
            context.startActivity(Intent(context, OpenAppAdActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        showAd(AdName.app_open, object : IAdCallBack {
            override fun onNoReady() {
                runnable?.run()
                runnable = null
                finish()
            }

            override fun onAdClose(status: Int) {
                runnable?.run()
                runnable = null
                finish()
            }
        })
    }
}