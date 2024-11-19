package com.theme.lambda.launcher.data.model

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

    @SerializedName("id")
    var id: String = ""

    @SerializedName("title")
    var title: String = ""

    @SerializedName("content")
    var text: String = ""

    @SerializedName("link")
    var url: String = ""

    @SerializedName("image_urls")
    var image: ArrayList<String> = arrayListOf()

    @SerializedName("publish_date")
    var publishDate: String = ""

    @SerializedName("author")
    var author: String = ""

    @SerializedName("language")
    var language: String = ""

    @SerializedName("country")
    var sourceCountry = ""

    @SerializedName("description")
    var sentiment = ""
}