package com.theme.lambda.launcher.ui.search

import android.app.SearchManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.os.UserHandle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.launcher3.AppInfo
import com.android.launcher3.ThemeManager
import com.android.launcher3.databinding.ActivitySearchBinding
import com.lambda.common.http.Preference
import com.lambda.common.utils.utilcode.util.ActivityUtils
import com.lambda.common.utils.utilcode.util.AppUtils
import com.lambda.common.utils.utilcode.util.GsonUtils
import com.lambda.common.utils.utilcode.util.Utils
import com.theme.lambda.launcher.base.BaseActivity
import com.theme.lambda.launcher.data.model.ManifestBean
import com.theme.lambda.launcher.utils.StatusBarUtil
import com.theme.lambda.launcher.utils.marginStatusBarHeight
import com.theme.lambda.launcher.utils.visible

class SearchActivity : BaseActivity<ActivitySearchBinding>() {
    private var searchHistory by Preference("search_history", "")
    private var searchHistoryList = mutableListOf<String>()
    private var recentAppsList = mutableListOf<String>()
    private val searchHistoryAdapter: SearchHistoryAdapter by lazy {
        SearchHistoryAdapter()
    }
    private val recentAppsAdapter: RecentAppsAdapter by lazy {
        RecentAppsAdapter()
    }
    private val localAppsAdapter: LocalAppsAdapter by lazy {
        LocalAppsAdapter()
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): ActivitySearchBinding {
        return ActivitySearchBinding.inflate(layoutInflater)
    }

    private val appsInfo by lazy {
        AppUtils.getAppsInfo()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)
        viewBinding.containerLl.marginStatusBarHeight()

        window.decorView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )
        initView()
    }

    private fun initView() {
        // 处理显示壁纸
        val curManifest = ThemeManager.getThemeManagerIfExist()?.getCurManifest()
        curManifest?.let {
            val wallpaper =
                ThemeManager.getThemeManagerIfExist()?.getManifestResRootPath() + it.background
            viewBinding.wallpaperView.setPic(wallpaper)
            viewBinding.wallpaperView.visible()
        }

        viewBinding.et.addTextChangedListener {
            if (it.isNullOrEmpty()) {
                viewBinding.ivClear.visibility = View.GONE
                viewBinding.ivSearch.visibility = View.GONE
            } else {
                viewBinding.ivClear.visibility = View.VISIBLE
                viewBinding.ivSearch.visibility = View.VISIBLE
            }
            val list = appsInfo.filter { appInfo ->
                ActivityUtils.getLauncherActivity(appInfo.packageName)
                    .isNotEmpty() && appInfo.name.contains(
                    it.toString(),
                    true
                ) && appInfo.packageName != packageName
            }.map { appInfo ->
                appInfo.packageName
            }
            viewBinding.clRecentApps.visibility =
                if (it.isNullOrEmpty() && recentAppsList.isNotEmpty()) View.VISIBLE else View.GONE
            viewBinding.clLocalApps.visibility =
                if (it.isNullOrEmpty() || list.isEmpty()) View.GONE else View.VISIBLE
            localAppsAdapter.setList(list)
        }
        viewBinding.et.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            viewBinding.clSearchHistory.visibility =
                if (hasFocus && searchHistoryList.isNotEmpty()) View.VISIBLE else View.GONE
        }
        viewBinding.ivClear.setOnClickListener {
            viewBinding.et.setText("")
            viewBinding.et.clearFocus()
        }
        viewBinding.ivSearch.setOnClickListener {
            searchHistoryList.add(0, viewBinding.et.text.toString())
            if (searchHistoryList.size > 10) {
                searchHistoryList = searchHistoryList.subList(0, 10)
            }
            searchHistory = GsonUtils.toJson(searchHistoryList)
            searchHistoryAdapter.setList(searchHistoryList)
            viewBinding.et.clearFocus()
            startGlobalSearch(viewBinding.et.text.toString())
        }
        viewBinding.ivDelete.setOnClickListener {
            searchHistoryList.clear()
            searchHistory = GsonUtils.toJson(searchHistoryList)
            searchHistoryAdapter.setList(searchHistoryList)
            viewBinding.clSearchHistory.visibility = View.GONE
        }
        if (searchHistory.isNotEmpty()) {
            searchHistoryList = GsonUtils.fromJson(
                searchHistory,
                GsonUtils.getListType(String::class.java)
            )
        }
        viewBinding.rvSearchHistory.adapter = searchHistoryAdapter
        viewBinding.rvSearchHistory.layoutManager =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        searchHistoryAdapter.setList(searchHistoryList)
        searchHistoryAdapter.setOnItemClickListener { _, _, position ->
            viewBinding.et.setText(searchHistoryAdapter.data[position])
            viewBinding.et.setSelection(searchHistoryAdapter.data[position].length)
        }
        if (recentApps.isNotEmpty()) {
            recentAppsList = GsonUtils.fromJson(
                recentApps,
                GsonUtils.getListType(String::class.java)
            )
        }
        viewBinding.clRecentApps.visibility =
            if (recentAppsList.isEmpty()) View.GONE else View.VISIBLE
        viewBinding.rvRecentApps.adapter = recentAppsAdapter
        viewBinding.rvRecentApps.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recentAppsAdapter.setList(recentAppsList)
        recentAppsAdapter.setOnItemClickListener { _, _, position ->
            viewBinding.et.clearFocus()
            AppUtils.launchApp(recentAppsAdapter.data[position])
            recentAppsAdapter.setList(addRecentApps(recentAppsAdapter.data[position]))
        }
        viewBinding.rvLocalApps.adapter = localAppsAdapter
        viewBinding.rvLocalApps.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        localAppsAdapter.setOnItemClickListener { _, _, position ->
            viewBinding.et.clearFocus()
            AppUtils.launchApp(localAppsAdapter.data[position])
            recentAppsAdapter.setList(addRecentApps(localAppsAdapter.data[position]))
        }
    }

    private fun startGlobalSearch(initialQuery: String?) {
        val searchManager =
            getSystemService(SEARCH_SERVICE) as SearchManager
        val globalSearchActivity = searchManager.globalSearchActivity ?: return
        val intent = Intent(SearchManager.INTENT_ACTION_GLOBAL_SEARCH)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setComponent(globalSearchActivity)
        if (!TextUtils.isEmpty(initialQuery)) {
            intent.putExtra(SearchManager.QUERY, initialQuery)
        }
        try {
            startActivity(intent)
        } catch (_: Exception) {
        }
    }

    companion object {
        private var recentApps by Preference("recent_apps", "")

        @JvmStatic
        fun addRecentApps(packageName: String): List<String> {
            var list = if (recentApps.isNotEmpty()) {
                GsonUtils.fromJson<MutableList<String>>(
                    recentApps,
                    GsonUtils.getListType(String::class.java)
                )
            } else {
                mutableListOf()
            }
            if (packageName == Utils.getApp().packageName) {
                return list
            }
            list.add(0, packageName)
            if (list.size > 10) {
                list = list.subList(0, 10)
            }
            recentApps = GsonUtils.toJson(list)
            return list
        }
    }

    private fun findActivity(
        apps: ArrayList<AppInfo>, component: ComponentName,
        user: UserHandle
    ): Boolean {
        val n = apps.size
        for (i in 0 until n) {
            val info = apps[i]
            if (info.user == user && info.componentName == component) {
                return true
            }
        }
        return false
    }
}