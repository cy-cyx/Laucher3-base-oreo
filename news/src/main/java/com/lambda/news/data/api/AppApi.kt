package com.lambda.news.data.api

import com.lambda.common.data.model.BaseResult
import com.lambda.news.data.model.NewResult
import com.lambda.news.data.model.NewsCategoriesResult
import retrofit2.http.GET
import retrofit2.http.Query

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

    @GET("news/v1/list_categories")
    suspend fun getListCategories(): BaseResult<NewsCategoriesResult>


}