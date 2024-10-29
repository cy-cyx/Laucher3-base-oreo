package com.theme.lambda.launcher.utils

import android.annotation.SuppressLint
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

object FirebaseConfigUtil : OnCompleteListener<Boolean> {

    private val TAG = "RemoteConfigUtil"

    var init = false

    @SuppressLint("StaticFieldLeak")
    private var firebaseRemoteConfig: FirebaseRemoteConfig? = null
    fun initRemoteConfig() {
        if (init) return
        init = true
        LogUtil.d(TAG, "init")
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder().apply {
            // 设置最小的刷新间隔
            minimumFetchIntervalInSeconds = 3600
        }.build()
        firebaseRemoteConfig?.setConfigSettingsAsync(configSettings)
        firebaseRemoteConfig?.setDefaultsAsync(getDefaultData())
        firebaseRemoteConfig?.fetchAndActivate()?.addOnCompleteListener(this)

    }

    fun getString(key: String) = firebaseRemoteConfig?.getString(key)
    fun getLong(key: String) = firebaseRemoteConfig?.getLong(key)


    override fun onComplete(p0: Task<Boolean>) {
        LogUtil.d(TAG, "init")
    }

    private fun getDefaultData(): HashMap<String, Any> {
        return HashMap<String, Any>().apply {
            put("app_open_wait_in_sec", 43200)
            put("app_open_interval_in_sec", 1800)
        }
    }
}