package com.theme.lambda.launcher.ui.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.theme.lambda.launcher.base.BaseViewModel
import com.theme.lambda.launcher.data.DataRepository
import com.theme.lambda.launcher.data.model.News
import kotlinx.coroutines.launch

class NewsViewModel : BaseViewModel() {

    var page = 0L

    var newsLiveData = MutableLiveData<ArrayList<News>>()
    var refreshFinishLiveData = MutableLiveData<Boolean>()
    var loadMoreFinishLiveData = MutableLiveData<Boolean>()
    var isLoadMore = false

    fun refresh() {
        viewModelScope.launch() {
            page = 1L
            val data = DataRepository.getNewData(page)
            val newsList = data?.d?.news ?: arrayListOf()
            newsLiveData.value = newsList
            refreshFinishLiveData.value = true
        }
    }

    fun loadMore() {
        if (isLoadMore) return
        isLoadMore = true

        viewModelScope.launch() {
            page++
            val data = DataRepository.getNewData(page)
            val newsList = data?.d?.news ?: arrayListOf()
            val allData = newsLiveData.value ?: arrayListOf()
            allData.addAll(newsList)
            newsLiveData.value = allData
            loadMoreFinishLiveData.value = true
            isLoadMore = false
        }
    }
}