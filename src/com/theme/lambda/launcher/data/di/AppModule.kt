package com.theme.lambda.launcher.data.di

import com.lambdaweather.AppViewModel
import com.lambdaweather.data.NetworkHandler
import com.lambdaweather.data.remote.AppRemoteData
import com.lambdaweather.data.remote.api.AppService
import com.lambdaweather.data.repository.AppRepository
import com.lambdaweather.data.repository.AppRepositorySource
import com.lambdaweather.ui.news.NewsViewModel
import com.lambda.common.net.RetrofitUtil
import com.theme.lambda.launcher.ui.weather.ui.SearchViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single { NetworkHandler(androidContext()) }

    single {
        RetrofitUtil.retrofit
    }
}

val dataModule = module {
    single { AppService(get()) }
    single<AppRepositorySource> {
        AppRepository(
            AppRemoteData(get(), get()),
            Dispatchers.IO
        )
    }
    factory { AppViewModel(get()) }
    factory { NewsViewModel(get()) }
    factory { SearchViewModel(get()) }
}

val allModules = appModule + dataModule