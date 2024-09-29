package com.theme.lambda.launcher.ui.permissionguide

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.android.launcher3.R
import java.lang.ref.WeakReference

class HomeLauncherSetGuideActivity : ComponentActivity() {

    companion object {

        var activity: WeakReference<HomeLauncherSetGuideActivity>? = null;

        fun stopAllDisplayPermissionGuideActivity() {
            activity?.get()?.finish()
            activity = null
        }

        fun start(context: Context) {
            val intent = Intent(context, HomeLauncherSetGuideActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_laucher_set_guide)

        setFinishOnTouchOutside(true)
        window.setBackgroundDrawable(BitmapDrawable())
        val attributes = window.attributes
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        attributes.gravity = Gravity.BOTTOM
        window.attributes = attributes

        findViewById<TextView>(R.id.titleTv).text =
            getString(R.string.home_launcher_permission_tip, getString(R.string.app_name))

        findViewById<FrameLayout>(R.id.containerFl).setOnClickListener {
            finish()
        }

        activity = WeakReference(this);
    }

    override fun onDestroy() {
        super.onDestroy()
        activity = null
    }
}