package com.theme.lambda.launcher.shortcut

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.theme.lambda.launcher.utils.AppUtil

class IntentGuideActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtil.openAppByName(this, intent?.getStringExtra("package").toString())
        finish()
    }
}