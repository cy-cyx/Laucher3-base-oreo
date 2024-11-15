package com.theme.lambda.launcher.data.model

import com.google.gson.annotations.SerializedName

class SearchInfo {
    @SerializedName("search_urls")
    var searchUrls = ArrayList<String>()

    @SerializedName("is_open_in_webview")
    var isOpenInWebView = true

    @SerializedName("shortcut_categories")
    var shortcut_categories = ArrayList<ShortCuts>()
}