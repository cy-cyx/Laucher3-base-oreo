package com.lambda.news.data.model

import com.google.gson.annotations.SerializedName

class NewsConfig {
    @SerializedName("enable_detail_top_ad")
    var enableDetailTopAd = true

    @SerializedName("enable_detail_bottom_ad")
    var enableDetailBottomAd = true

    @SerializedName("detail_ad_interval")
    var detailAdInterval = 10

    @SerializedName("list_ad_interval")
    var listAdInterval = 5
}