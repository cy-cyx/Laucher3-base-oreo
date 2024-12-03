package com.lambda.news.ui.detail

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.lambda.common.base.BaseViewModel
import com.lambda.common.utils.CommonUtil
import com.lambda.common.utils.GsonUtil
import com.lambda.news.data.DataRepository
import com.lambda.news.data.model.News
import com.lambda.news.data.model.NewsConfig
import com.lambda.remoteconfig.LambdaRemoteConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsDetailViewModel : BaseViewModel() {

    val moreNewsLiveData = MediatorLiveData<ArrayList<News>>()
    val newsConfig: NewsConfig by lazy {
        val string =
            LambdaRemoteConfig.getInstance(CommonUtil.appContext!!).getString("NewsConfig")
        GsonUtil.gson.fromJson(string, NewsConfig::class.java)
    }

    var new: News? = null

    fun loadMoreNews() {
        viewModelScope.launch(Dispatchers.IO) {

            val data = DataRepository.getNewData(1, new?.categories?.firstOrNull() ?: "")
            var newsList = data?.news ?: arrayListOf()

            // 排除自己随机选三个
            newsList = newsList.filter { it.id != (new?.id ?: "") }
                .toMutableList() as java.util.ArrayList<News>
            if (newsList.size > 3) {

                var temp = ArrayList<News>()
                for (i in 0 until 3) {
                    val news = newsList.random()
                    newsList.remove(news)
                    temp.add(news)
                }
                moreNewsLiveData.postValue(temp)
            } else {
                moreNewsLiveData.postValue(newsList)
            }
        }
    }
}