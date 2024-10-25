package com.theme.lambda.launcher.shortcut

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle

class IntentGuideActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val shortcutInfoIntent = this@IntentGuideActivity.packageManager.getLaunchIntentForPackage(
            intent?.getStringExtra("package").toString()
        )
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                shortcutInfoIntent?.action = Intent.ACTION_VIEW
            }
            startActivity(shortcutInfoIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        finish()
    }
}