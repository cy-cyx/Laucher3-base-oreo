package com.theme.lambda.launcher.service

import android.os.Bundle
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.theme.lambda.launcher.statistics.EventName
import com.theme.lambda.launcher.statistics.EventUtil.logEvent
import com.theme.lambda.launcher.utils.LogUtil
import com.theme.lambda.launcher.utils.SpKey
import com.theme.lambda.launcher.utils.getMMKVLong

class FirebaseService : FirebaseMessagingService() {

    companion object {
        private val TAG = "FBM"
        fun subscribe() {
            val index = SpKey.install_time.getMMKVLong(0) % 10
            val topic = "all$index"
            LogUtil.d(TAG, "subscribe topic : $topic")
            FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener {
                LogUtil.d(TAG, "subscribe ${it.isSuccessful}")
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        LogUtil.d(TAG, "onMessageReceived : $message")
        logEvent(EventName.AppAlive, Bundle())
    }

}
