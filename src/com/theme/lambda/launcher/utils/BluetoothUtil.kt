package com.theme.lambda.launcher.utils

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

object BluetoothUtil : BroadcastReceiver() {

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }

    fun init(context: Context?) {
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
    }
}
