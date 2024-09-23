package com.theme.lambda.launcher.ui.theme

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.launcher3.Launcher
import com.android.launcher3.ThemeManager
import com.theme.lambda.launcher.base.BaseViewModel
import com.theme.lambda.launcher.data.DataRepository
import com.theme.lambda.launcher.data.model.Resources
import com.theme.lambda.launcher.task.DownloadZipTask
import com.theme.lambda.launcher.ui.themepreview.ThemePreviewActivity
import com.theme.lambda.launcher.utils.requestTag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ThemeViewModel : BaseViewModel() {

    var tag = ""
    var from = ""
    var page = 0L

    var themeLiveData = MutableLiveData<ArrayList<Resources>>()
    var refreshFinishLiveData = MutableLiveData<Boolean>()
    var loadMoreFinishLiveData = MutableLiveData<Boolean>()
    var loadDialogLiveData = MutableLiveData<Boolean>()
    var isLoadMore = false

    fun refresh() {
        viewModelScope.launch() {
            page = 1L
            val data = DataRepository.getResource(page, tag.requestTag())
            val resList = data?.resources ?: arrayListOf()
            themeLiveData.value = resList
            refreshFinishLiveData.value = true
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

    fun downloadAndGotoPreview(context: Activity, resources: Resources) {
        // 首页进来和主题进来处理不同
        if (from == ThemeActivity.sFromSplash) {
            viewModelScope.launch(Dispatchers.IO) {
                loadDialogLiveData.postValue(true)

                val downloadZipTask = DownloadZipTask(resources)
                val result = downloadZipTask.execute()
                loadDialogLiveData.postValue(false)
                if (result) {
                    ThemeManager.setDefaultTHemeId(resources.id)
                    context.finish()
                    context.startActivity(Intent(context, Launcher::class.java))
                    DataRepository.insertDownLoadThemeIntoDb(resources.toThemeRes())
                } else {
                    Toast.makeText(context, "download error!!", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            ThemePreviewActivity.start(context,resources)
        }

    }
}