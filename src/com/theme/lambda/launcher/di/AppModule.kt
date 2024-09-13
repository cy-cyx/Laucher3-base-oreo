package com.theme.lambda.launcher.di

import android.util.Log
import com.lambdaweather.AppViewModel
import com.lambdaweather.data.LenientGsonConverterFactory
import com.lambdaweather.data.NetworkHandler
import com.lambdaweather.data.remote.AppRemoteData
import com.lambdaweather.data.remote.api.AppService
import com.lambdaweather.data.repository.AppRepository
import com.lambdaweather.data.repository.AppRepositorySource
import com.lambdaweather.ui.news.NewsViewModel
import com.lambdaweather.utils.GsonUtil
import com.theme.lambda.launcher.Constants
import kotlinx.coroutines.Dispatchers
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

val appModule = module {

    single { NetworkHandler(androidContext()) }

    single {
        Retrofit.Builder()
            .client( RetrofitUrlManager.getInstance().with(
                OkHttpClient.Builder()
                    .hostnameVerifier { _, _ -> true }
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(HttpLoggingInterceptor { message ->
                        Log.d(
                            "RetrofitFactory",
                            "message：$message"
                        )
                    }.setLevel(HttpLoggingInterceptor.Level.BODY))
            ).build()
            )
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(LenientGsonConverterFactory.create(GsonUtil.mGson))
            .build()
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
}

val allModules = appModule + dataModule