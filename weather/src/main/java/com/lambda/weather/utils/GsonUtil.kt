package com.lambdaweather.utils

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Author ZhengQinHong
 * Date 2017/3/22
 */
object GsonUtil {
    val mGson = GsonBuilder().serializeSpecialFloatingPointValues()
        .create()

    /**
     * 生产jsonString
     */
    fun toJson(`object`: Any?): String {
        return if (null == mGson) "" else mGson.toJson(`object`)
    }

    /**
     * jsonString 转 任意类型
     */
    fun <T> fromJson(jsonString: String?, clazz: Class<T>?): T? {
        return if (null == mGson) null else mGson.fromJson(jsonString, clazz)
    }

    /**
     * jsonString 转 任意类型
     */
    fun <T> fromJson(jsonString: String?, type: Type?): T {
        return mGson!!.fromJson(jsonString, type)
    }

    /**
     * jsonString 转 List
     *
     * @param jsonString
     * @param <T>
     * @return
    </T> */
    fun <T> fromJson(jsonString: String): List<T>? {
        val type =
            object : TypeToken<List<T>>() {}.type
        return mGson.fromJson(jsonString, type)
    }

    /**
     * 两重Json转list
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
    </T> */
    fun <T> fromJsonList(json: String?, cls: Class<T>?): List<T> {
        val mList: MutableList<T> = ArrayList()
        val array = JsonParser.parseString(json).asJsonArray
        for (elem in array) {
            mList.add(mGson!!.fromJson(elem, cls))
        }
        return mList
    }
}