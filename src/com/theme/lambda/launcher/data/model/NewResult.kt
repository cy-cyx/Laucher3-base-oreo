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
    val id: String = ""

    @SerializedName("title")
    val title: String = ""

    @SerializedName("content")
    val text: String = ""

    @SerializedName("link")
    val url: String = ""

    @SerializedName("image_urls")
    val image: ArrayList<String> = arrayListOf()

    @SerializedName("publish_date")
    val publishDate: String = ""

    @SerializedName("author")
    val author: String = ""

    @SerializedName("language")
    val language: String = ""

    @SerializedName("country")
    val sourceCountry = ""

    @SerializedName("description")
    val sentiment = ""
}