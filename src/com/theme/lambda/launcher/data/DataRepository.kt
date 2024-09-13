package com.theme.lambda.launcher.data

import com.theme.lambda.launcher.data.api.AppApi
import com.theme.lambda.launcher.data.http.RetrofitUtil
import com.theme.lambda.launcher.data.model.NewResult
import com.theme.lambda.launcher.utils.TimeUtil
import java.util.Locale

object DataRepository {

    private val service by lazy {
        RetrofitUtil.retrofit.create(AppApi::class.java)
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

            )
        } catch (e: Exception) {
        }
        return null
    }
}