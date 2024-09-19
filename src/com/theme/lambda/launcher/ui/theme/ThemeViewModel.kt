package com.theme.lambda.launcher.ui.theme

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.theme.lambda.launcher.base.BaseViewModel
import com.theme.lambda.launcher.data.DataRepository
import com.theme.lambda.launcher.data.model.Resources
import com.theme.lambda.launcher.utils.requestTag
import kotlinx.coroutines.launch

class ThemeViewModel : BaseViewModel() {

    var tag = ""
    var page = 0L

    var themeLiveData = MutableLiveData<ArrayList<Resources>>()
    var refreshFinishLiveData = MutableLiveData<Boolean>()
    var loadMoreFinishLiveData = MutableLiveData<Boolean>()
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

}