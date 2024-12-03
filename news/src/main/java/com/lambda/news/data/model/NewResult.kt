package com.lambda.news.data.model

import com.google.gson.annotations.SerializedName

class NewResult {
    @SerializedName("news")
    val news = ArrayList<News>()

    @SerializedName("total_num")
    val totalNum: Int = 0

    @SerializedName("total_page")
    val totalPage: Int = 0
}

class News {

    @SerializedName("author")
    var author: String = ""

    @SerializedName("categories")
    var categories: ArrayList<String> = arrayListOf()

    @SerializedName("content")
    var text: String = ""

    @SerializedName("country")
    var sourceCountry = ""

    @SerializedName("description")
    var sentiment = ""

    @SerializedName("id")
    var id: String = ""

    @SerializedName("image_urls")
    var image: ArrayList<String> = arrayListOf()

    @SerializedName("language")
    var language: String = ""

    @SerializedName("link")
    var url: String = ""

    @SerializedName("publish_date")
    var publishDate: String = ""

    @SerializedName("title")
    var title: String = ""

    @SerializedName("source")
    var source: String = ""

    @SerializedName("source_icon")
    var sourceIcon = ""
}