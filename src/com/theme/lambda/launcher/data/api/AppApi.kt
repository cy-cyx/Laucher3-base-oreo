package com.theme.lambda.launcher.data.api

import com.theme.lambda.launcher.data.model.BaseResult
import com.theme.lambda.launcher.data.model.NewResult
import com.theme.lambda.launcher.data.model.ResResult
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface AppApi {

    @GET("news/v1/search_news")
    suspend fun getNewData(
        @Query("countries") countries: String?,
        @Query("languages") languages: String?,
        @Query("page") page: String?,
        @Query("page_size") pageSize: String?,
        @Query("categories") categories: String?,
        @Query("publish_date_from") publishDateFrom: String?,
        @Query("sort") sort: String?,
        @Query("fallback") fallback: String?,
    ): BaseResult<NewResult>

    @GET("api/v2/query_tag_resources")
    suspend fun getResource(
        @QueryMap map: Map<String, String>
    ): BaseResult<ResResult>
}