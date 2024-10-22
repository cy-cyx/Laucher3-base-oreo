package com.theme.lambda.launcher.utils

import android.content.Context
import androidx.core.app.NotificationManagerCompat

object NotificationUtil {
    fun notificationsEnabled(context: Context) =
        NotificationManagerCompat.from(context).areNotificationsEnabled()
}