package com.theme.lambda.launcher.utils

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.theme.lambda.launcher.appwidget.widget.XPanelAppWidget

object BluetoothUtil : BroadcastReceiver() {

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }

    var init = false

    fun init(context: Context?) {
        if (init) return
        init = true
        var intentFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        context?.registerReceiver(this, intentFilter);
    }

    fun isEnabled(): Boolean {
        return try {
            bluetoothAdapter.isEnabled
        } catch (e: Exception) {
            false
        }
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        XPanelAppWidget.upData()
    }
}
