package com.theme.lambda.launcher.ui.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity.SEARCH_SERVICE
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lambda.common.http.Preference
import com.lambda.common.utils.utilcode.util.ActivityUtils
import com.lambda.common.utils.utilcode.util.AppUtils
import com.lambda.common.utils.utilcode.util.GsonUtils
import com.lambda.remoteconfig.LambdaRemoteConfig
import com.theme.lambda.launcher.base.BaseViewModel
import com.theme.lambda.launcher.data.model.FileInfo
import com.theme.lambda.launcher.data.model.SearchInfo
import com.theme.lambda.launcher.statistics.EventName
import com.theme.lambda.launcher.statistics.EventUtil
import com.theme.lambda.launcher.ui.search.SearchActivity.Companion.addRecentApps
import com.theme.lambda.launcher.ui.search.SearchActivity.Companion.recentApps
import com.theme.lambda.launcher.ui.search.searchlib.FileSearchLib
import com.theme.lambda.launcher.ui.search.searchlib.NetSearchLib
import com.theme.lambda.launcher.ui.search.searchlib.PicSearchLib
import com.theme.lambda.launcher.ui.web.WebViewActivity
import com.theme.lambda.launcher.utils.AppUtil
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.GsonUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.URLEncoder

class SearchViewModel : BaseViewModel() {
    private var searchHistory by Preference("search_history", "")

    private var appsInfo = arrayListOf<AppUtils.AppInfo>()
    val recentAppLiveData = MutableLiveData<ArrayList<String>>(arrayListOf())
    val searchHistoryLiveData = MutableLiveData<ArrayList<String>>(arrayListOf())
    val localAppLiveData = MutableLiveData<ArrayList<String>>(arrayListOf())
    val searchModeLiveData = MutableLiveData<Boolean>(false)
    val netUrlLiveData = MutableLiveData<ArrayList<String>>(arrayListOf())
    val imageLiveData = MutableLiveData<ArrayList<FileInfo>>(arrayListOf())
    val fileLiveData = MutableLiveData<ArrayList<FileInfo>>(arrayListOf())

    private val searchInfo: SearchInfo by lazy {
        val string =
            LambdaRemoteConfig.getInstance(CommonUtil.appContext!!).getString("SearchConfig")
        GsonUtil.gson.fromJson(string, SearchInfo::class.java)
    }

    fun initData() {
        viewModelScope.launch(Dispatchers.IO) {
            if (recentApps.isNotEmpty()) {
                val temp: ArrayList<String> = GsonUtils.fromJson(
                    recentApps,
                    GsonUtils.getListType(String::class.java)
                )
                recentAppLiveData.postValue(temp.filter {
                    AppUtil.checkAppInstalled(
                        CommonUtil.appContext,
                        it
                    )
                }.toMutableList() as ArrayList)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            if (searchHistory.isNotEmpty()) {
                searchHistoryLiveData.postValue(
                    GsonUtils.fromJson(
                        searchHistory,
                        GsonUtils.getListType(String::class.java)
                    )
                )
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            // 这样预加载，不然首次查找巨久
            appsInfo = AppUtils.getAppsInfo().toMutableList() as ArrayList<AppUtils.AppInfo>
        }
    }

    private var searchLocolAppJob: Job? = null

    fun searchLocalApp(string: String) {
        if (string.isEmpty()) return
        searchLocolAppJob?.cancel()
        searchLocolAppJob = viewModelScope.launch(Dispatchers.IO) {
            val list = appsInfo.filter { appInfo ->
                ActivityUtils.getLauncherActivity(appInfo.packageName)
                    .isNotEmpty() && appInfo.name.contains(
                    string,
                    true
                ) && appInfo.packageName != CommonUtil.appContext!!.packageName
            }.map { appInfo ->
                appInfo.packageName
            }
            localAppLiveData.postValue(list as ArrayList<String>)
        }
    }

    private var searchNetUrlJob: Job? = null

    fun searchNetUrl(string: String) {
        if (string.isEmpty()) return
        searchNetUrlJob?.cancel()
        searchNetUrlJob = viewModelScope.launch(Dispatchers.IO) {
            netUrlLiveData.postValue(NetSearchLib.findNetUrl(string))
        }
    }

    private var searchImageJob: Job? = null
    fun searchImage(string: String) {
        if (string.isEmpty()) return
        searchImageJob = viewModelScope.launch(Dispatchers.IO) {
            imageLiveData.postValue(PicSearchLib.findImages(string))
        }
    }

    private var searchFileJob: Job? = null
    fun searchFile(string: String) {
        if (string.isEmpty()) return
        searchFileJob = viewModelScope.launch(Dispatchers.IO) {
            fileLiveData.postValue(FileSearchLib.findFiles(string))
        }
    }


    fun search(context: Context, string: String) {
        startGlobalSearch(context, string)
        viewModelScope.launch(Dispatchers.IO) {
            var list = searchHistoryLiveData.value ?: arrayListOf()
            list.add(0, string)
            if (list.size > 10) {
                list = list.subList(0, 10).toMutableList() as ArrayList<String>
            }
            searchHistory = GsonUtils.toJson(list)
            searchHistoryLiveData.postValue(list)
        }
    }

    fun cleanSearchHistory() {
        searchHistoryLiveData.value = arrayListOf()
        searchHistory = GsonUtil.gson.toJson(emptyList<String>())
    }

    private fun startGlobalSearch(context: Context, query: String?) {
        if (searchInfo.searchUrls.isNotEmpty()) {
            var sUrl = searchInfo.searchUrls.random()
            sUrl = sUrl.replace("{query_string}", URLEncoder.encode(query ?: ""))

            if (searchInfo.isOpenInWebView) {
                WebViewActivity.start(context, sUrl)
            } else {
                CommonUtil.openWebView(context, sUrl)
            }
        } else {
            val searchManager =
                context.getSystemService(SEARCH_SERVICE) as SearchManager
            val globalSearchActivity = searchManager.globalSearchActivity ?: return
            val intent = Intent(SearchManager.INTENT_ACTION_GLOBAL_SEARCH)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setComponent(globalSearchActivity)
            if (!TextUtils.isEmpty(query)) {
                intent.putExtra(SearchManager.QUERY, query)
            }
            try {
                context.startActivity(intent)
            } catch (_: Exception) {
            }
        }
        EventUtil.logEvent(EventName.LWebSearch, Bundle())
    }

    fun clickApp(app: String) {
        AppUtils.launchApp(app)
        viewModelScope.launch(Dispatchers.IO) {
            val apps = addRecentApps(app)
            recentAppLiveData.postValue(apps.filter {
                AppUtil.checkAppInstalled(
                    CommonUtil.appContext,
                    it
                )
            }.toMutableList() as ArrayList)
        }
    }
}