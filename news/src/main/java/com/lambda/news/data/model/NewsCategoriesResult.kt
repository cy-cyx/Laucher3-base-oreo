package com.lambda.news.data.model

import com.google.gson.annotations.SerializedName

class NewsCategoriesResult {
    @SerializedName("categories")
    var categories: ArrayList<String> = arrayListOf()
}