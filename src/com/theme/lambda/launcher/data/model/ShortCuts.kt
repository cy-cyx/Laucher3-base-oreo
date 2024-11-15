package com.theme.lambda.launcher.data.model

import com.google.gson.annotations.SerializedName


class ShortCuts(
    @SerializedName("category")
    val category: String = "",

    @SerializedName("shortcuts")
    var shortcuts: ArrayList<ShortCut> = arrayListOf()
) {
    fun copy(): ShortCuts {
        val temp = ArrayList<ShortCut>()
        shortcuts.forEach {
            temp.add(it.copy())
        }
        return ShortCuts(category, temp)
    }
}

class ShortCut(
    @SerializedName("name")
    var name: String = "",

    @SerializedName("icon_url")
    var iconUrl: String = "",

    @SerializedName("click_url")
    var clickUrl: String = "",

    @SerializedName("is_default")
    var isDefault: Boolean = false
) {
    // 是否是选择
    var isSelect: Boolean = false

    // 是否是加号
    var isAdd: Boolean = false

    var isEdit:Boolean = false

    fun copy(): ShortCut {
        return ShortCut(name, iconUrl, clickUrl, isDefault)
    }
}