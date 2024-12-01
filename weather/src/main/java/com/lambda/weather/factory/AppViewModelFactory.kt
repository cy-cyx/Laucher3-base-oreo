package com.lambdaweather.factory

import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lambda.weather.AppViewModel
import com.lambdaweather.data.repository.AppRepositorySource

class AppViewModelFactory(private val parameter: AppRepositorySource) : ViewModelProvider.Factory {

    @NonNull
    override fun <T : ViewModel> create(@NonNull modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            return AppViewModel(parameter) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}