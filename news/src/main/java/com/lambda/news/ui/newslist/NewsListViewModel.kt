package com.lambda.news.ui.newslist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lambda.common.base.BaseItem
import com.lambda.common.base.BaseViewModel
import com.lambda.common.vip.VipManager
import com.lambda.news.data.DataRepository
import com.lambda.news.data.model.News
import com.lambda.news.ui.newslist.item.AdItem
import com.lambda.news.ui.newslist.item.NewsItem
import kotlinx.coroutines.launch

class NewsListViewModel : BaseViewModel() {

    var page = 0L

    var category = ""

    private val adInterval = 5
    private var curAdInterval = 3 // 间隔是5个 开始为2个故从3个起

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
            val data = DataRepository.getNewData(page)
            val newsList = data?.news ?: arrayListOf()
            newsLiveData.value = assembleData(newsList, true)
            refreshFinishLiveData.value = true
        }
    }

    fun loadMore() {
        if (isLoadMore) return
        isLoadMore = true

        viewModelScope.launch() {
            page++
            val data = DataRepository.getNewData(page)
            val newsList = data?.news ?: arrayListOf()
            val allData = newsLiveData.value ?: arrayListOf()
            allData.addAll(assembleData(newsList, false))
            newsLiveData.value = allData
            loadMoreFinishLiveData.value = true
            isLoadMore = false
        }
    }

    // 混入ad广告位
    private fun assembleData(newsList: ArrayList<News>, upData: Boolean): ArrayList<BaseItem> {
        if (upData) {
            curAdInterval = 3
        }

        val list = ArrayList<BaseItem>()
        newsList.forEach {
            curAdInterval++
            list.add(NewsItem(it))
            if (curAdInterval >= adInterval) {
                // vip去掉广告
                if (VipManager.isVip.value == false) {
                    list.add(AdItem())
                }
                curAdInterval = 0
            }
        }
        return list
    }
}