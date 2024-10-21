package com.theme.lambda.launcher.ui.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity.SEARCH_SERVICE
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lambda.common.http.Preference
import com.lambda.common.utils.utilcode.util.ActivityUtils
import com.lambda.common.utils.utilcode.util.AppUtils
import com.lambda.common.utils.utilcode.util.GsonUtils
import com.theme.lambda.launcher.base.BaseViewModel
import com.theme.lambda.launcher.ui.search.SearchActivity.Companion.addRecentApps
import com.theme.lambda.launcher.ui.search.SearchActivity.Companion.recentApps
import com.theme.lambda.launcher.utils.AppUtil
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.GsonUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel : BaseViewModel() {
    private var searchHistory by Preference("search_history", "")

    private var appsInfo = arrayListOf<AppUtils.AppInfo>()
    val recentAppLiveData = MutableLiveData<ArrayList<String>>(arrayListOf())
    val searchHistoryLiveData = MutableLiveData<ArrayList<String>>(arrayListOf())
    val localAppLiveData = MutableLiveData<ArrayList<String>>(arrayListOf())

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

    private var searchJob: Job? = null

    fun searchLocalApp(string: String) {
        if (string.isEmpty()) return
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
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

    fun search(context: Context, string: String) {
        startGlobalSearch(context, string)
        viewModelScope.launch(Dispatchers.IO) {
            var list = searchHistoryLiveData.value ?: arrayListOf()
            list.add(0, string)
            if (list.size > 10) {
                list = list.subList(0, 10) as ArrayList<String>
            }
            searchHistory = GsonUtils.toJson(list)
            searchHistoryLiveData.postValue(list)
        }
    }

    fun cleanSearchHistory() {
        searchHistoryLiveData.value = arrayListOf()
        searchHistory = GsonUtil.gson.toJson(emptyList<String>())
    }

    private fun startGlobalSearch(context: Context, initialQuery: String?) {
        val searchManager =
            context.getSystemService(SEARCH_SERVICE) as SearchManager
        val globalSearchActivity = searchManager.globalSearchActivity ?: return
        val intent = Intent(SearchManager.INTENT_ACTION_GLOBAL_SEARCH)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setComponent(globalSearchActivity)
        if (!TextUtils.isEmpty(initialQuery)) {
            intent.putExtra(SearchManager.QUERY, initialQuery)
        }
        try {
            context.startActivity(intent)
        } catch (_: Exception) {
        }
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