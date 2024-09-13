package com.theme.lambda.launcher.data.model

import com.google.gson.annotations.SerializedName

class NewResult {
    @SerializedName("status")
    val status: String = ""

    @SerializedName("c")
    val code: Int = 0

    @SerializedName("d")
    val d: Data = Data()

    class Data {
        @SerializedName("news")
        val news = ArrayList<News>()

        @SerializedName("total_num")
        val totalNum: Int = 0

        @SerializedName("total_page")
        val totalPage: Int = 0
    }
}