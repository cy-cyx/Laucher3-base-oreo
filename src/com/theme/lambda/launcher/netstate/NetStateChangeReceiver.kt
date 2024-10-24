package com.theme.lambda.launcher.netstate

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.theme.lambda.launcher.ad.AdUtil
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.LogUtil

class NetStateChangeReceiver : BroadcastReceiver() {

    private val TAG = "NetStateChangeReceiver"

    companion object {
        fun registerReceiver(context: Context) {
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
    }
}