package com.theme.lambda.launcher.data.model

import com.google.gson.annotations.SerializedName

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