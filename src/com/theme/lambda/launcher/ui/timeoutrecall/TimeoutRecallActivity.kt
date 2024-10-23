package com.theme.lambda.launcher.ui.timeoutrecall

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.theme.lambda.launcher.ui.splash.SplashActivity
import com.theme.lambda.launcher.ui.theme.ThemeActivity
import com.theme.lambda.launcher.utils.LauncherUtil

class TimeoutRecallActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isTaskRoot) {
            finish()
        } else {
            if (LauncherUtil.isDefaultLauncher(this)) {
                ThemeActivity.start(this, ThemeActivity.sFromTheme)
            } else {
                SplashActivity.start(this)
            }
            finish()
        }
    }
}