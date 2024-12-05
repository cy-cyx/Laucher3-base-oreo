package com.android.launcher3.custom

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.android.launcher3.Launcher
import com.android.launcher3.databinding.LayoutCustomViewBinding
import com.lambda.common.statistics.EventName
import com.lambda.common.statistics.EventUtil
import com.lambda.common.utils.PermissionUtil

class CustomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs), Launcher.CustomContentCallbacks {


    init {
        LayoutCustomViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun onShow(fromResume: Boolean) {
        EventUtil.logEvent(EventName.LNews, Bundle().apply {
            putString("from", "slide")
        })


        requestNotificationPermission()
    }

    override fun onHide() {

    }

    override fun onScrollProgressChanged(progress: Float) {

    }

    override fun isScrollingAllowed(): Boolean {
        return true;
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionUtil.requestRuntimePermissions(
                context,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                object : PermissionUtil.IPermissionCallback {
                    override fun nextStep() {
                        EventUtil.logEvent(EventName.permissionGrant, Bundle().apply {
                            putString("scene", "news")
                            putString("permission", "notification")
                        })
                    }

                    override fun noPermission() {

                    }

                    override fun gotoSet(internal: Boolean) {

                    }
                },
                force = false,
                showGotoSetDialog = false
            )
        }
    }

}