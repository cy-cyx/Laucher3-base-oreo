package com.lambda.news.ui.newslist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lambda.common.base.BaseItem
import com.lambda.common.base.BaseViewModel
import com.lambda.news.data.DataRepository
import com.lambda.news.ui.newslist.item.NewsItem
import kotlinx.coroutines.launch

class NewsListViewModel : BaseViewModel() {

    var page = 0L

    var category = ""

    private fun getQueryCategory(): String {
        if (category == "Local") {
            return ""
        } else {
            return category
        }
    }

    var newsLiveData = MutableLiveData<ArrayList<BaseItem>>()
    var refreshFinishLiveData = MutableLiveData<Boolean>()
    var loadMoreFinishLiveData = MutableLiveData<Boolean>()
    var isLoadMore = false

    fun refresh() {
        viewModelScope.launch() {
            page = 1L
            val data = DataRepository.getNewData(page, getQueryCategory())
            val newsList = data?.news ?: arrayListOf()
            newsLiveData.value =
                newsList.map { NewsItem(it) }.toMutableList() as ArrayList<BaseItem>
            refreshFinishLiveData.value = true
        }
    }

    fun loadMore() {
        if (isLoadMore) return
        isLoadMore = true

        viewModelScope.launch() {
            page++
            val data = DataRepository.getNewData(page, getQueryCategory())
            val newsList = data?.news ?: arrayListOf()
            val allData = newsLiveData.value ?: arrayListOf()
            allData.addAll(newsList.map { NewsItem(it) }.toMutableList())
            newsLiveData.value = allData
            loadMoreFinishLiveData.value = true
            isLoadMore = false
        }
    }
}