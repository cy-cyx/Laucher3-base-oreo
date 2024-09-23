package com.theme.lambda.launcher.data

import androidx.room.Room
import com.lambda.common.http.RequestParam
import com.theme.lambda.launcher.Constants
import com.theme.lambda.launcher.data.api.AppApi
import com.theme.lambda.launcher.data.http.RetrofitUtil
import com.theme.lambda.launcher.data.model.NewResult
import com.theme.lambda.launcher.data.model.ResResult
import com.theme.lambda.launcher.data.model.ThemeRes
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.TimeUtil
import java.util.Locale

object DataRepository {

    private val service by lazy {
        RetrofitUtil.retrofit.create(AppApi::class.java)
    }

    private val db by lazy {
        Room.databaseBuilder(
            CommonUtil.appContext!!,
            LauncherDataBase::class.java,
            "launcher_um_db"
        ).build()
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

    suspend fun getResource(page: Long, tag: String): ResResult? {

        try {
            val params: MutableMap<String, String> =
                mutableMapOf(
                    "r_tag" to tag,
                    "page" to page.toString(),
                    "page_size" to "20",
                    "sort_by" to "2",
                    "r_types" to "1"
                )
            return service.getResource(
                RequestParam.Builder().build().buildParam(params, Constants.SECRET_KEY, false)
            ).d
        } catch (e: Exception) {
        }
        return null
    }

    fun insertDownLoadThemeIntoDb(themeRes: ThemeRes) {
        db.themeResDao().insert(themeRes)
    }

    fun getDownLoadThemeRecord(): List<ThemeRes> {
        return db.themeResDao().getDownLoadThemeResList()
    }
}