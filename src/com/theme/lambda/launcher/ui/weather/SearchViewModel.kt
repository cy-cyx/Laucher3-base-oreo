package com.theme.lambda.launcher.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lambdaweather.data.Resource
import com.lambdaweather.data.model.CityModel
import com.lambdaweather.data.repository.AppRepositorySource
import kotlinx.coroutines.launch

class SearchViewModel(private val appRepositorySource: AppRepositorySource) : ViewModel() {
    val geo = MutableLiveData<Resource<List<CityModel>>>()

    fun getGeo(lat: String?, lon: String?, city: String?) {
        viewModelScope.launch {
            appRepositorySource.getGeo(lat, lon, q = city).collect {
                geo.value = it
            }
        }
    }
}