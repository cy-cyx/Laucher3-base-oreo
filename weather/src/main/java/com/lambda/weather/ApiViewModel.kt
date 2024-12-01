
package com.lambda.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lambdaweather.data.Resource
import com.lambdaweather.data.model.EarthModel
import com.lambdaweather.data.model.NewsModel
import com.lambdaweather.data.model.OneCallModel
import com.lambdaweather.data.repository.AppRepositorySource
import kotlinx.coroutines.launch

abstract class ApiViewModel(val appRepositorySource: AppRepositorySource) : ViewModel() {
    val newList = MutableLiveData<Resource<NewsModel>>()
    val earthquake = MutableLiveData<EarthModel>()
    var mNewsCount = 1
    val oneCall = MutableLiveData<Resource<OneCallModel>>()

    fun getNewsNext(lat: String, lon: String, country: String) {
        mNewsCount++
        getNews(lat, lon, country)
    }

    fun initNewsNext(lat: String, lon: String, country: String) {
        mNewsCount = 1
        getNews(lat, lon, country)
    }

    fun getNews(lat: String, lon: String, country: String) {
        viewModelScope.launch {
            appRepositorySource.getNews(
                country,
                mNewsCount.toString()
            ).collect {
                it.data?.pageL = mNewsCount
                newList.value = it
                if (it.data?.d?.news?.size == 0) {
                    mNewsCount = 1
                }
            }
        }
    }

    fun getAlert(lat: String, lon: String) {
        viewModelScope.launch {
            appRepositorySource.getAlert(
                lat, lon
            ).collect {
                oneCall.value = it
            }
        }
    }
}