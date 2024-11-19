package com.lambdaweather.data.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import com.theme.lambda.launcher.data.model.News

class NewsModel {

    @SerializedName("c")
    val c: Int? = null

    @SerializedName("d")
    val d: D? = null


    var pageL: Int? = null

    data class D(
        @SerializedName("news")
        val news: List<NewsDTO>? = null,

        val total_num: Int,
        val total_page: Int
    )

    class NewsDTO : MultiItemEntity {
        override val itemType: Int
            get() = if (isAd == true) 1 else 0

        var isAd: Boolean? = null

        @SerializedName("categories")
        var categories: List<String>? = null

        @SerializedName("content")
        var content: String? = null

        @SerializedName("country")
        var country: String? = null

        @SerializedName("description")
        var description: String? = null

        @SerializedName("image_urls")
        var image_urls: List<String>? = null

        @SerializedName("link")
        var link: String? = null

        @SerializedName("video_urls")
        var video_urls: List<String>? = null

        @SerializedName("id")
        var id: String? = null

        @SerializedName("title")
        val title: String? = null

        @SerializedName("publish_date")
        val publishDate: String? = null

        @SerializedName("author")
        val author: String? = null

        @SerializedName("language")
        val language: String? = null

        fun toNews(): News {
            val new = News()
            new.let {
                it.id = id ?: ""
                it.title = title ?: ""
                it.publishDate = publishDate ?: ""
                it.url = link ?: ""
                it.image = (image_urls ?: arrayListOf<String>()) as ArrayList<String>
                it.author = author ?: ""
                it.text = content ?: ""
                it.language = language ?: ""
            }
            return new
        }
    }
}