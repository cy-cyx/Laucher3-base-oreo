package com.theme.lambda.launcher.service

import android.os.Bundle
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lambda.common.utils.getSpLong
import com.lambda.common.statistics.EventName
import com.lambda.common.statistics.EventUtil.logEvent
import com.lambda.common.utils.LogUtil
import com.lambda.common.utils.SpKey

class FirebaseService : FirebaseMessagingService() {

    companion object {
        private val TAG = "FBM"
        fun subscribe() {
            val index = SpKey.install_time.getSpLong(0) % 10
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
