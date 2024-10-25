package com.theme.lambda.launcher.shortcut

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast


class InstallShortCutSuccessBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        Toast.makeText(p0, "set icon success!!", Toast.LENGTH_SHORT).show()
    }
}