package com.theme.lambda.launcher.ui.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lambda.common.base.BaseItem
import com.lambda.common.base.BaseViewModel
import com.lambda.common.utils.CommonUtil
import com.lambda.common.utils.GsonUtil
import com.lambda.common.vip.VipManager
import com.lambda.remoteconfig.LambdaRemoteConfig
import com.theme.lambda.launcher.data.DataRepository
import com.theme.lambda.launcher.data.model.News
import com.theme.lambda.launcher.data.model.NewsConfig
import com.theme.lambda.launcher.ui.news.item.AdItem
import com.theme.lambda.launcher.ui.news.item.NewsItem
import kotlinx.coroutines.launch

class NewsViewModel : BaseViewModel() {

    var page = 0L
    private var curAdInterval = 3 // 间隔是5个 开始为2个故从3个起

    var newsLiveData = MutableLiveData<ArrayList<BaseItem>>()
    var refreshFinishLiveData = MutableLiveData<Boolean>()
    var loadMoreFinishLiveData = MutableLiveData<Boolean>()
    private var isLoadMore = false
    private val newsConfig: NewsConfig by lazy {
        val string =
            LambdaRemoteConfig.getInstance(CommonUtil.appContext!!).getString("NewsConfig")
        GsonUtil.gson.fromJson(string, NewsConfig::class.java)
    }

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