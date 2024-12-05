package com.lambda.news.ui.newslist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lambda.common.base.BaseItem
import com.lambda.common.base.BaseViewModel
import com.lambda.common.utils.CommonUtil
import com.lambda.common.utils.GsonUtil
import com.lambda.common.vip.VipManager
import com.lambda.news.data.DataRepository
import com.lambda.news.data.model.News
import com.lambda.news.data.model.NewsConfig
import com.lambda.news.ui.newslist.item.AdItem
import com.lambda.news.ui.newslist.item.NewsItem
import com.lambda.remoteconfig.LambdaRemoteConfig
import kotlinx.coroutines.launch

class NewsListViewModel : BaseViewModel() {

    var page = 0L

    var category = ""

    private val newsConfig: NewsConfig by lazy {
        val string =
            LambdaRemoteConfig.getInstance(CommonUtil.appContext!!).getString("NewsConfig") ?: ""
        GsonUtil.gson.fromJson(string, NewsConfig::class.java) ?: NewsConfig()
    }
    private var curAdInterval = 3 // 间隔是5个 开始为2个故从3个起

    private fun getQueryCategory(): String {
        if (category == "Local") {
            return ""
        } else if (category == "Headlines") {
            return "top"
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
            newsList.shuffle()
            newsLiveData.value = assembleData(newsList, true)
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
            newsList.shuffle()
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
            if (curAdInterval >= newsConfig.listAdInterval) {
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