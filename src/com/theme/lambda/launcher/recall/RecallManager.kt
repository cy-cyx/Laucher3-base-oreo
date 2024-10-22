package com.theme.lambda.launcher.recall

import android.Manifest
import android.app.Activity
import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service.NOTIFICATION_SERVICE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.launcher3.BaseActivity
import com.android.launcher3.R
import com.theme.lambda.launcher.ui.timeoutrecall.TimeoutRecallActivity
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.LauncherUtil.isDefaultLauncher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

object RecallManager : Application.ActivityLifecycleCallbacks {

    private var recallTime = 2 * 60 * 1000L

    private var timeoutRecallJob: Job? = null

    fun init(app: Application) {
        app.registerActivityLifecycleCallbacks(this)
    }

    // default home app授权时，或点击应用内广告时，开启120s的定时器
    fun startTimeoutRecall(context: Context) {
        timeoutRecallJob = GlobalScope.launch {
            delay(recallTime)
            if (isDefaultLauncher(context)) return@launch
            sendNotification(context)
        }
    }

    private fun sendNotification(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                CommonUtil.appContext!!, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        try {
            NotificationManagerCompat.from(CommonUtil.appContext!!)
                .notify(100, createRecallNotification(context))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createRecallNotification(context: Context): Notification {
        val notificationManager =
            CommonUtil.appContext!!.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, TimeoutRecallActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(
                context,
                UUID.randomUUID().hashCode(),
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

        // Android8.0以上的系统，新建消息通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //通道的重要程度
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel =
                NotificationChannel(
                    "timeout_recall",
                    "timeout_recall",
                    importance
                )
            notificationChannel.description = "timeout_recall"
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val builder = NotificationCompat.Builder(context, "timeout_recall")
        builder.setContentText(context.getString(R.string.default_home_app_callback))
        builder.setSmallIcon(R.mipmap.ic_launcher_notification)
        builder.setWhen(System.currentTimeMillis())
        builder.priority = NotificationCompat.PRIORITY_MAX
        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(true)
        builder.setGroup("timeout_recall")
        return builder.build()
    }


    private fun stopTimeoutRecall() {
        timeoutRecallJob?.cancel()
        timeoutRecallJob = null
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {
        if (activity is BaseActivity || activity is com.theme.lambda.launcher.base.BaseActivity<*>) {
            stopTimeoutRecall()
        }
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }
}