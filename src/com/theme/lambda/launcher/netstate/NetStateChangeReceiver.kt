package com.theme.lambda.launcher.netstate

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.lambda.common.ad.AdUtil
import com.theme.lambda.launcher.appwidget.widget.XPanelAppWidget
import com.lambda.common.utils.CommonUtil
import com.lambda.common.utils.LogUtil

class NetStateChangeReceiver : BroadcastReceiver() {

    private val TAG = "NetStateChangeReceiver"

    companion object {
        var init = false
        fun registerReceiver(context: Context) {
            if (init) return
            init = true
            val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            context.registerReceiver(NetStateChangeReceiver(), intentFilter);
        }

    }

    private var curNetWorkType = NetworkType.NETWORK_EMPTY

    override fun onReceive(p0: Context?, p1: Intent?) {
        val networkType = NetStateUtil.getNetworkType(CommonUtil.appContext!!)
        LogUtil.d(TAG, "net state change! : $networkType")
        if (networkType == NetworkType.NETWORK_NO) {

        } else {
            if (curNetWorkType == NetworkType.NETWORK_NO) {
                // 网络恢复
                AdUtil.reLoadIfNeed()
            }
        }
        curNetWorkType = networkType
        XPanelAppWidget.upData()
    }
}