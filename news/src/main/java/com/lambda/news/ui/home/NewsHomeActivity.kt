package com.lambda.news.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import com.lambda.common.base.BaseActivity
import com.lambda.common.statistics.EventName
import com.lambda.common.statistics.EventUtil
import com.lambda.common.utils.PermissionUtil
import com.lambda.common.utils.StatusBarUtil
import com.lambda.common.utils.marginStatusBarHeight
import com.lambda.news.R
import com.lambda.news.databinding.NewsActivityHomeBinding

class NewsHomeActivity : BaseActivity<NewsActivityHomeBinding>() {

    companion object {
        val sKeyFrom = "sKeyFrom"

        val sFromHome = 1  // 来源从首页进入
        val sFromCustom = 2  // 来源从负一屏进入
        val sFromWeather = 3  // 来源从天气进入
        val sFromSearch = 4  // 来源从搜索进入

        fun start(context: Context, from: Int) {
            context.startActivity(Intent(context, NewsHomeActivity::class.java).apply {
                putExtra(sKeyFrom, from)
            })
        }
    }

    var openFrom = sFromHome

    override fun initViewBinding(layoutInflater: LayoutInflater): NewsActivityHomeBinding {
        return NewsActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)
        viewBinding.containerFl.marginStatusBarHeight()

        openFrom = intent.getIntExtra(sKeyFrom, sFromHome)

        try {
            supportFragmentManager.fragments.forEach {
                supportFragmentManager.beginTransaction().remove(it).commitNow()
            }
        } catch (e: Exception) {
        }

        supportFragmentManager.beginTransaction().apply {
            add(R.id.containerFl, NewsHomeFragment().apply {
                mfrom = openFrom
            }, "home")
        }.commitNow()

        requestNotificationPermission()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionUtil.requestRuntimePermissions(
                this,
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