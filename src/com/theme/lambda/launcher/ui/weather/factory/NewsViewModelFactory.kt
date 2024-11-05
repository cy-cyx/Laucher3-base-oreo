package com.lambdaweather.factory

import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lambdaweather.AppViewModel
import com.lambdaweather.data.repository.AppRepositorySource
import com.lambdaweather.ui.news.NewsViewModel

class NewsViewModelFactory(private val parameter: AppRepositorySource) : ViewModelProvider.Factory {

    @NonNull
    override fun <T : ViewModel> create(@NonNull modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(parameter) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}