package com.theme.lambda.launcher.shortcut

import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.android.launcher3.R
import com.theme.lambda.launcher.utils.CommonUtil

object ShortCutUtil {

    fun addShortCut(
        packageName: String,
        icon: String,
        title: String
    ) {
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(CommonUtil.appContext!!)) {
            val shortcutInfoIntent =
                Intent(CommonUtil.appContext!!, IntentGuideActivity::class.java)
            shortcutInfoIntent.action = Intent.ACTION_VIEW //action必须设置，不然报错
            shortcutInfoIntent.putExtra("package", packageName)

            val info =
                ShortcutInfoCompat.Builder(
                    CommonUtil.appContext!!,
                    "${packageName}_${System.currentTimeMillis()}"
                ).setIcon(IconCompat.createWithBitmap(BitmapFactory.decodeFile(icon)))
                    .setShortLabel(title.ifEmpty { "app" })
                    .setIntent(shortcutInfoIntent)
                    .build()
            val shortcutCallbackIntent = PendingIntent.getBroadcast(
                CommonUtil.appContext!!,
                5,
                Intent(
                    CommonUtil.appContext!!,
                    InstallShortCutSuccessBroadcastReceiver::class.java
                ),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            ShortcutManagerCompat.requestPinShortcut(
                CommonUtil.appContext!!,
                info,
                shortcutCallbackIntent.intentSender
            )
        } else {
            Toast.makeText(CommonUtil.appContext!!, R.string.fail_icon_hint, Toast.LENGTH_LONG)
                .show()
        }
    }
}