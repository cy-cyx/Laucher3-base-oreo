package com.theme.lambda.launcher.urlshortcut

import com.android.launcher3.R
import com.google.gson.reflect.TypeToken
import com.lambda.remoteconfig.LambdaRemoteConfig
import com.theme.lambda.launcher.data.model.SearchInfo
import com.theme.lambda.launcher.data.model.ShortCut
import com.theme.lambda.launcher.data.model.ShortCuts
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.GsonUtil
import com.theme.lambda.launcher.utils.SpKey
import com.theme.lambda.launcher.utils.getSpString
import com.theme.lambda.launcher.utils.putSpString

object UrlShortcutManager {

    val shortCutTag = arrayListOf(
        "Social",
        "Ecommerce",
        "Video",
        "News",
        "Tools",
        "Entertainment",
        "Music",
        "Sports",
        "Travel",
        "Health"
    )

    fun getIconByTag(tag: String): Int {
        return when (tag) {
            "Social" -> R.drawable.ic_shortcut_social
            "Ecommerce" -> R.drawable.ic_shortcut_shopping
            "Video" -> R.drawable.ic_shortcut_video
            "News" -> R.drawable.ic_shortcut_news
            "Entertainment" -> R.drawable.ic_shortcut_entertainment
            "Music" -> R.drawable.ic_shortcut_music
            "Sports" -> R.drawable.ic_shortcut_sport
            "Travel" -> R.drawable.ic_shortcut_travel
            "Health" -> R.drawable.ic_shortcut_healthy
            else -> R.drawable.ic_shortcut_tools
        }
    }

    private val shortcuts: ArrayList<ShortCuts> by lazy {
        val string =
            LambdaRemoteConfig.getInstance(CommonUtil.appContext!!).getString("SearchConfig")
        val json = GsonUtil.gson.fromJson(string, SearchInfo::class.java)
        json.shortcut_categories
    }

    fun getShortCuts(): ArrayList<ShortCuts> {
        val temp = ArrayList<ShortCuts>()
        shortcuts.forEach {
            temp.add(it.copy())
        }
        return temp
    }

    private var curShortCut = ArrayList<ShortCut>()

    fun getCurShortCut(): ArrayList<ShortCut> {

        if (curShortCut.isNotEmpty()) {
            return curShortCut
        }

        if (SpKey.keyCurUrlShortCur.getSpString().isNotEmpty()) {
            val sShortCut = SpKey.keyCurUrlShortCur.getSpString()
            val typeToken = object : TypeToken<List<ShortCut>>() {}
            curShortCut.addAll(GsonUtil.gson.fromJson(sShortCut, typeToken).toMutableList())
        } else {
            val string =
                LambdaRemoteConfig.getInstance(CommonUtil.appContext!!).getString("SearchConfig")
            val json = GsonUtil.gson.fromJson(string, SearchInfo::class.java)

            val temp = ArrayList<ShortCut>()
            json.shortcut_categories.forEach { d ->
                d.shortcuts.forEach { shortcut ->
                    if (shortcut.isDefault) {
                        temp.add(shortcut)
                    }
                }
            }
            curShortCut.addAll(temp)

            // 存一份本地
            SpKey.keyCurUrlShortCur.putSpString(GsonUtil.gson.toJson(curShortCut))
        }
        return curShortCut
    }

    fun upDataCurShortCut(data:ArrayList<ShortCut>){
        curShortCut = data
        SpKey.keyCurUrlShortCur.putSpString(GsonUtil.gson.toJson(curShortCut))
    }
}