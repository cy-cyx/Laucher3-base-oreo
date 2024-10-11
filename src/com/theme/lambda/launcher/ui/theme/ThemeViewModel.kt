package com.theme.lambda.launcher.ui.theme

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.reflect.TypeToken
import com.theme.lambda.launcher.base.BaseViewModel
import com.theme.lambda.launcher.data.DataRepository
import com.theme.lambda.launcher.data.model.Resources
import com.theme.lambda.launcher.ui.themepreview.ThemePreviewActivity
import com.theme.lambda.launcher.utils.GsonUtil
import com.theme.lambda.launcher.utils.LauncherUtil
import com.theme.lambda.launcher.utils.SpUtil
import com.theme.lambda.launcher.utils.requestTag
import com.theme.lambda.launcher.widget.dialog.ApplyLauncherPermissionDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ThemeViewModel : BaseViewModel() {

    companion object {
        val sKeyCache = "key_cache_"
    }


    var tag = ""
    var from = ""
    var page = 0L

    var themeLiveData = MutableLiveData<ArrayList<Resources>>()
    var refreshFinishLiveData = MutableLiveData<Boolean>()
    var loadMoreFinishLiveData = MutableLiveData<Boolean>()
    var loadDialogLiveData = MutableLiveData<Boolean>()
    var isLoadMore = false

    fun refreshCache() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cache = SpUtil.getString("sKeyCache$tag")
                if (cache.isNotEmpty()) {
                    val typeToken = object : TypeToken<List<Resources>>() {}
                    themeLiveData.postValue(ArrayList(GsonUtil.gson.fromJson(cache, typeToken)))
                }

            } catch (e: Exception) {

            }
        }
    }

    fun refresh() {
        viewModelScope.launch() {
            page = 1L
            val data = DataRepository.getResource(page, tag.requestTag())
            val resList = data?.resources ?: arrayListOf()
            themeLiveData.value = resList
            refreshFinishLiveData.value = true

            if (resList.isNotEmpty()) {
                SpUtil.putString("sKeyCache$tag", GsonUtil.gson.toJson(resList))
            }
        }
    }

    fun loadMore() {
        if (isLoadMore) return
        isLoadMore = true

        viewModelScope.launch() {
            page++
            val data = DataRepository.getResource(page, tag.requestTag())
            val resList = data?.resources ?: arrayListOf()
            val allData = themeLiveData.value ?: arrayListOf()
            allData.addAll(resList)
            themeLiveData.value = allData
            loadMoreFinishLiveData.value = true
            isLoadMore = false
        }
    }

    fun gotoPreview(context: Activity, resources: Resources) {
        if (!LauncherUtil.isDefaultLauncher(context)) {
            ApplyLauncherPermissionDialog(context).apply {
                clickApplyListen = {
                    dismiss()
                    ThemePreviewActivity.start(context, resources)
                    LauncherUtil.gotoSetLauncher(context)
                }
                clickNotNowListen = {
                    dismiss()
                    ThemePreviewActivity.start(context, resources)
                }
            }.show()
        } else {
            ThemePreviewActivity.start(context, resources)
        }
    }
}