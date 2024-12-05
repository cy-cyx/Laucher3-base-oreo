package com.theme.lambda.launcher.ui.search

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.launcher3.ThemeManager
import com.android.launcher3.databinding.ActivitySearchBinding
import com.lambda.common.ad.AdName
import com.lambda.common.base.BaseActivity
import com.lambda.common.http.Preference
import com.lambda.common.statistics.EventName
import com.lambda.common.statistics.EventUtil
import com.lambda.common.utils.GsonUtil
import com.lambda.common.utils.PermissionUtil
import com.lambda.common.utils.SpKey
import com.lambda.common.utils.StatusBarUtil
import com.lambda.common.utils.getSpString
import com.lambda.common.utils.gone
import com.lambda.common.utils.marginStatusBarHeight
import com.lambda.common.utils.utilcode.util.GsonUtils
import com.lambda.common.utils.utilcode.util.Utils
import com.lambda.common.utils.visible
import com.lambda.news.ui.detail.NewsDetailActivity
import com.lambda.news.ui.home.NewsHomeActivity
import com.lambda.weather.view.WeatherNewBanner.OnRvBannerClickListener
import com.lambdaweather.data.model.NewsModel
import com.theme.lambda.launcher.ui.search.adapter.FileAdapter
import com.theme.lambda.launcher.ui.search.adapter.ImageAdapter
import com.theme.lambda.launcher.ui.search.adapter.ItemTouchHelperCallback
import com.theme.lambda.launcher.ui.search.adapter.LocalAppsAdapter
import com.theme.lambda.launcher.ui.search.adapter.NetUrlAdapter
import com.theme.lambda.launcher.ui.search.adapter.RecentAppsAdapter
import com.theme.lambda.launcher.ui.search.adapter.SearchHistoryAdapter
import com.theme.lambda.launcher.ui.search.adapter.UrlShortcutAdapter
import com.theme.lambda.launcher.ui.search.adapter.YourMayLikeAdapter
import com.theme.lambda.launcher.ui.search.searchlib.FileSearchLib
import com.theme.lambda.launcher.ui.search.searchlib.PicSearchLib

class SearchActivity : BaseActivity<ActivitySearchBinding>() {

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, SearchActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
        }

        var recentApps by Preference("recent_apps", "")

        var recentAppLiveData = MediatorLiveData<ArrayList<String>>()

        @JvmStatic
        fun addRecentApps(packageName: String): List<String> {
            var list = if (recentApps.isNotEmpty()) {
                GsonUtils.fromJson<MutableList<String>>(
                    recentApps, GsonUtils.getListType(String::class.java)
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
            recentAppLiveData.postValue(ArrayList(list))
            return list
        }

        @JvmStatic
        fun initRecentApps() {
            var list = if (recentApps.isNotEmpty()) {
                GsonUtils.fromJson<MutableList<String>>(
                    recentApps, GsonUtils.getListType(String::class.java)
                )
            } else {
                mutableListOf()
            }
            recentAppLiveData.value = list as ArrayList<String>?
        }
    }

    private val searchHistoryAdapter: SearchHistoryAdapter by lazy {
        SearchHistoryAdapter()
    }
    private val recentAppsAdapter: RecentAppsAdapter by lazy {
        RecentAppsAdapter()
    }

    private val localAppsAdapter: LocalAppsAdapter by lazy {
        LocalAppsAdapter()
    }

    private val netUrlAdapter: NetUrlAdapter by lazy {
        NetUrlAdapter()
    }

    private val imageAdapter: ImageAdapter by lazy {
        ImageAdapter()
    }
    private val fileAdapter: FileAdapter by lazy {
        FileAdapter()
    }

    private val shortCutAdapter: UrlShortcutAdapter by lazy {
        UrlShortcutAdapter()
    }

    private val itemTouchHelperCallback: ItemTouchHelperCallback by lazy {
        ItemTouchHelperCallback()
    }

    private val yourMayLikeAdapter: YourMayLikeAdapter by lazy {
        YourMayLikeAdapter()
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
        initView()
        initData()

        // 图片和文件需要请求权限
        requestPermissionAndLoadData()
        EventUtil.logEvent(EventName.LWeb, Bundle())
    }

    private fun requestPermissionAndLoadData() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        PermissionUtil.requestRuntimePermissions(
            this, permissions, object : PermissionUtil.IPermissionCallback {
                override fun nextStep() {
                    PicSearchLib.loadData()
                    // 再处理文件请求
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                        val i = SpKey.requestDocPermissionTime.getSpInt(0)
//                        if (!Environment.isExternalStorageManager() &&
//                            /*事不过三*/i < 3
//                        ) {
//                            SpKey.requestDocPermissionTime.putSpInt(i + 1)
//                            ApplyDocumentPermissionDialog(this@SearchActivity).apply {
//                                clickApplyListen = {
//                                    dismiss()
//                                    PermissionUtil.gotoFileAccessPage(this@SearchActivity)
//                                }
//                                clickNotNowListen = {
//                                    dismiss()
//                                }
//                            }.show()
//                        } else {
//                            FileSearchLib.loadData()
//                        }
//                    } else {
                    FileSearchLib.loadData()
//                    }
                    viewBinding.root.postDelayed({
                        showKeyBoard(viewBinding.et)
                    }, 1000)
                }

                override fun noPermission() {

                }

                override fun gotoSet(internal: Boolean) {

                }
            }, force = false, showGotoSetDialog = false
        )
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
            FileSearchLib.loadData()
        }
    }

    private fun initView() {
        // 处理显示壁纸
        val curManifest = ThemeManager.getThemeManagerIfExist()?.getCurManifest()
        curManifest?.let {
            val wallpaper =
                ThemeManager.getThemeManagerIfExist()?.getManifestResRootPath() + it.background
            if (SpKey.curUserWallpaperId.getSpString() != ThemeManager.getThemeManagerIfExist()?.themeId) {
                viewBinding.wallpaperView.setPic(wallpaper)
                viewBinding.wallpaperView.visible()
            }
        }

        viewBinding.rvSearchHistory.adapter = searchHistoryAdapter
        viewBinding.rvSearchHistory.layoutManager =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

        viewBinding.rvRecentApps.adapter = recentAppsAdapter
        viewBinding.rvRecentApps.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recentAppsAdapter.setOnItemClickListener { _, _, position ->
            viewBinding.et.clearFocus()
            viewBinding.et.setText("")
            viewModel.clickApp(recentAppsAdapter.data[position])
        }

        viewBinding.rvLocalApps.adapter = localAppsAdapter
        viewBinding.rvLocalApps.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        localAppsAdapter.setOnItemClickListener { _, _, position ->
            viewBinding.et.clearFocus()
            viewBinding.et.setText("")
            viewModel.clickApp(localAppsAdapter.data[position])
        }

        viewBinding.netUrlRv.adapter = netUrlAdapter
        viewBinding.netUrlRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        viewBinding.imageRv.adapter = imageAdapter
        viewBinding.imageRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        viewBinding.fileRv.adapter = fileAdapter
        viewBinding.fileRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        viewBinding.et.addTextChangedListener {
            if (it.isNullOrEmpty()) {
                viewBinding.ivClear.visibility = View.GONE
                viewBinding.ivSearch.visibility = View.GONE
                viewBinding.searchOnNetFl.gone()
            } else {
                viewBinding.ivClear.visibility = View.VISIBLE
                viewBinding.ivSearch.visibility = View.VISIBLE
                viewBinding.searchOnNetFl.visible()
            }
            viewModel.searchModeLiveData.value = it?.isNotEmpty() ?: false
            viewModel.searchLocalApp(it.toString())
            viewModel.searchNetUrl(it.toString())
            viewModel.searchImage(it.toString())
            viewModel.searchFile(it.toString())
        }
        viewBinding.et.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            viewBinding.clSearchHistory.visibility =
                if (hasFocus && searchHistoryAdapter.data.isNotEmpty()) View.VISIBLE else View.GONE
        }
        viewBinding.et.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewBinding.et.clearFocus()
                viewModel.search(this@SearchActivity, viewBinding.et.text.toString())
            }
            true
        }

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
        searchHistoryAdapter.setOnItemClickListener { _, _, position ->
            viewBinding.et.setText(searchHistoryAdapter.data[position])
            viewBinding.et.setSelection(searchHistoryAdapter.data[position].length)
        }
        viewBinding.searchOnNetFl.setOnClickListener {
            viewBinding.et.clearFocus()
            viewModel.search(this, viewBinding.et.text.toString())
        }

        viewBinding.rvUrlShortcut.apply {
            layoutManager = GridLayoutManager(context, 5)
            adapter = shortCutAdapter
        }
        shortCutAdapter.longClickListen = {
            if (!it.isAdd && !it.isPlaceholder) {
                viewModel.enterShortCutEdit()
                !viewModel.isShortCutEdit
            }
        }

        shortCutAdapter.clickListen = {
            if (it.isEdit) {
                if (it.isAdd) {
                    // 不存在
                } else {
                    viewModel.deleteShortCut(it)
                }
            } else if (it.isPlaceholder) {
                viewModel.quitShortCutEdit()
            } else {
                if (it.isAdd) {
                    viewModel.clickAddShortCut(this)
                } else {
                    viewModel.clickShortCut(it)
                }
            }
        }

        itemTouchHelperCallback.onSwapListen = { it1, it2 ->
            if (it2 < shortCutAdapter.data.size - 1) {
                viewModel.swapShortCut(it1, it2)
                shortCutAdapter.notifyItemMoved(it1, it2)
                true
            } else {
                false
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(viewBinding.rvUrlShortcut)
        viewBinding.clWeather.setOnClickListener {
            NewsHomeActivity.start(this, NewsHomeActivity.sFromSearch)
            EventUtil.logEvent(EventName.LNews, Bundle().apply {
                putString("from", "web")
            })
        }
        viewBinding.rvBanner.setOnRvBannerClickListener(object : OnRvBannerClickListener {
            override fun onClick(date: NewsModel.NewsDTO) {
                NewsDetailActivity.start(
                    this@SearchActivity,
                    GsonUtil.gson.toJson(date),
                    NewsHomeActivity.sFromSearch
                )
                EventUtil.logEvent(EventName.LNewsClick, Bundle().apply {
                    putString("from", "web")
                })
            }
        })

        viewBinding.rvYourMayLike.apply {
            layoutManager = GridLayoutManager(this@SearchActivity, 5)
            adapter = yourMayLikeAdapter
        }

        viewBinding.rvBanner.from = com.lambda.weather.view.WeatherNewBanner.fromSearch

        viewBinding.mrecBanner.scenesName = AdName.search_mrec
        viewBinding.mrecBanner.loadAd()
    }

    private fun initData() {
        viewModel.searchModeLiveData.observe(this, Observer {
            if (it) {
                viewBinding.inSearchModeSv.visible()
                viewBinding.noInSearchModeSv.gone()
            } else {
                viewBinding.inSearchModeSv.gone()
                viewBinding.noInSearchModeSv.visible()
            }
        })

        viewModel.recentAppLiveData.observe(this, Observer {
            viewBinding.clRecentApps.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            recentAppsAdapter.setList(it)
        })
        viewModel.searchHistoryLiveData.observe(this, Observer {
            searchHistoryAdapter.setList(it)
        })
        viewModel.localAppLiveData.observe(this, Observer {
            viewBinding.clLocalApps.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            localAppsAdapter.setList(it)
        })
        viewModel.netUrlLiveData.observe(this, Observer {
            viewBinding.netUrlLl.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            netUrlAdapter.setList(it)
        })
        viewModel.imageLiveData.observe(this, Observer {
            viewBinding.picLl.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            imageAdapter.setList(it)
        })
        viewModel.fileLiveData.observe(this, Observer {
            viewBinding.fileLl.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            fileAdapter.setList(it)
        })
        viewModel.shortcutLiveData.observe(this, Observer {
            shortCutAdapter.setList(it)
            viewBinding.clUrlShortcut.visible()
        })
        viewModel.yourMayLikeLiveData.observe(this, Observer {
            viewBinding.clYourMayLike.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            yourMayLikeAdapter.setList(it)
        })
        viewModel.newList.observe(this, Observer {
            if ((it.data?.d?.news?.size ?: 0) > 4) {
                viewBinding.clWeather.visibility = View.VISIBLE

                viewBinding.rvBanner.setRvBannerData(
                    it.data?.d?.news?.subList(0, 4)
                )
            }
        })
        viewModel.initData()
    }

    override fun onBackPressed() {
        if (viewModel.isShortCutEdit) {
            viewModel.quitShortCutEdit()
            return
        }

        super.onBackPressed()
    }
}