package com.theme.lambda.launcher.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.launcher3.ThemeManager
import com.android.launcher3.databinding.ActivitySearchBinding
import com.lambda.common.http.Preference
import com.lambda.common.utils.utilcode.util.GsonUtils
import com.lambda.common.utils.utilcode.util.Utils
import com.theme.lambda.launcher.base.BaseActivity
import com.theme.lambda.launcher.ui.search.adapter.LocalAppsAdapter
import com.theme.lambda.launcher.ui.search.adapter.RecentAppsAdapter
import com.theme.lambda.launcher.ui.search.adapter.SearchHistoryAdapter
import com.theme.lambda.launcher.utils.StatusBarUtil
import com.theme.lambda.launcher.utils.gone
import com.theme.lambda.launcher.utils.marginStatusBarHeight
import com.theme.lambda.launcher.utils.visible
import okhttp3.internal.cache.DiskLruCache

class SearchActivity : BaseActivity<ActivitySearchBinding>() {
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

    private val viewModel by viewModels<SearchViewModel>()

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
        initData()
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
            viewBinding.clRecentApps.visibility =
                if (it.isNullOrEmpty() && recentAppsAdapter.data.isNotEmpty()) View.VISIBLE else View.GONE
            viewBinding.clLocalApps.visibility =
                if (it.isNullOrEmpty() || localAppsAdapter.data.isEmpty()) View.GONE else View.VISIBLE

            viewModel.searchLocalApp(it.toString())
        }
        viewBinding.et.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            viewBinding.clSearchHistory.visibility =
                if (hasFocus && searchHistoryAdapter.data.isNotEmpty()) View.VISIBLE else View.GONE
        }
        viewBinding.et.setOnEditorActionListener(object : OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewBinding.et.clearFocus()
                    viewModel.search(this@SearchActivity, viewBinding.et.text.toString())
                }
                return true
            }
        })
        viewBinding.ivClear.setOnClickListener {
            viewBinding.et.setText("")
            viewBinding.et.clearFocus()
        }
        viewBinding.ivSearch.setOnClickListener {
            viewBinding.et.clearFocus()
            viewModel.search(this, viewBinding.et.text.toString())
        }
        viewBinding.ivDelete.setOnClickListener {
            viewModel.cleanSearchHistory()
            viewBinding.clSearchHistory.visibility = View.GONE
        }

        viewBinding.rvSearchHistory.adapter = searchHistoryAdapter
        viewBinding.rvSearchHistory.layoutManager =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

        searchHistoryAdapter.setOnItemClickListener { _, _, position ->
            viewBinding.et.setText(searchHistoryAdapter.data[position])
            viewBinding.et.setSelection(searchHistoryAdapter.data[position].length)
        }

        viewBinding.rvRecentApps.adapter = recentAppsAdapter
        viewBinding.rvRecentApps.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recentAppsAdapter.setOnItemClickListener { _, _, position ->
            viewBinding.et.clearFocus()
            viewModel.clickApp(recentAppsAdapter.data[position])
        }

        viewBinding.rvLocalApps.adapter = localAppsAdapter
        viewBinding.rvLocalApps.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        localAppsAdapter.setOnItemClickListener { _, _, position ->
            viewBinding.et.clearFocus()
            viewModel.clickApp(localAppsAdapter.data[position])
        }
    }

    private fun initData() {
        viewModel.recentAppLiveData.observe(this, Observer {
            viewBinding.clRecentApps.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            recentAppsAdapter.setList(it)
        })
        viewModel.searchHistoryLiveData.observe(this, Observer {
            searchHistoryAdapter.setList(it)
        })
        viewModel.localAppLiveData.observe(this, Observer {
            localAppsAdapter.setList(it)
            if (viewBinding.et.text.isNotEmpty() && it.isNotEmpty()) {
                viewBinding.clLocalApps.visible()
            } else {
                viewBinding.clLocalApps.gone()
            }
        })

        viewModel.initData()
    }

    companion object {
        var recentApps by Preference("recent_apps", "")

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
            list.remove(packageName)
            list.add(0, packageName)
            if (list.size > 10) {
                list = list.subList(0, 10)
            }
            recentApps = GsonUtils.toJson(list)
            return list
        }
    }
}