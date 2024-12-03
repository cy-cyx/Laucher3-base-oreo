package com.lambda.news.data

import com.lambda.common.net.RetrofitUtil
import com.lambda.common.utils.TimeUtil
import com.lambda.news.data.api.AppApi
import com.lambda.news.data.model.NewResult
import java.util.Locale

object DataRepository {

    private val service by lazy {
        RetrofitUtil.retrofit.create(AppApi::class.java)
    }

    suspend fun getNewsCategories(): ArrayList<String> {
        try {
            return service.getListCategories().d?.categories ?: arrayListOf<String>()
        } catch (e: Exception) {

        }
        return arrayListOf()
    }

    suspend fun getNewData(page: Long, categories: String = ""): NewResult? {
        try {
            return service.getNewData(
                Locale.getDefault().country,
                Locale.getDefault().language,
                "$page",
                "25",
                categories,
                "${TimeUtil.getOldDate(-365)}",
                "publish_date,desc",
                "true"

            ).d
        } catch (e: Exception) {
        }
        return null
    }
}