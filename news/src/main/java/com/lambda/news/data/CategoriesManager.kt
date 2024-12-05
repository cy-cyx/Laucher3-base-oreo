package com.lambda.news.data

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import com.google.gson.reflect.TypeToken
import com.lambda.common.utils.GsonUtil
import com.lambda.common.utils.SpKey
import com.lambda.common.utils.getSpString
import com.lambda.common.utils.putSpString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object CategoriesManager {

    var TAG = "TagManager"

    // 当前的
    var allCategories = ArrayList<String>()

    // 全部的
    var myCategoriesLiveData = MediatorLiveData<ArrayList<String>>()

    fun init() {
        GlobalScope.launch(Dispatchers.IO) {
            myCategoriesLiveData.postValue(getMyCategoriesCache())

            var categories = DataRepository.getNewsCategories()
            categories = categories.filter { it.isNotBlank() && !it.equals("top") } as ArrayList<String>
            Log.d(TAG, "$categories")
            allCategories = categories

            val myCategories = myCategoriesLiveData.value ?: arrayListOf()
            if (myCategories.isEmpty()) {
                var index = 0
                for (it in allCategories) {
                    myCategories.add(it)
                    index++
                    // 空的话，默认塞5个进去
                    if (index > 4) {
                        break
                    }
                }
                myCategoriesLiveData.postValue(myCategories)
            } else {
                // 过滤一下当前的
                myCategoriesLiveData.postValue(myCategories.filter { allCategories.contains(it) } as ArrayList<String>?)
            }
        }
    }

    private fun getMyCategoriesCache(): ArrayList<String> {
        try {
            val s = SpKey.keyMyCategories.getSpString()
            val typeToken = object : TypeToken<List<String>>() {}
            return GsonUtil.gson.fromJson(s, typeToken) as ArrayList<String>
        } catch (e: Exception) {

        }
        return arrayListOf()
    }

    private fun cacheMyCategories(data: ArrayList<String>) {
        SpKey.keyMyCategories.putSpString(GsonUtil.gson.toJson(data))
    }

    fun upDataMyCategories(categories: ArrayList<String>) {
        myCategoriesLiveData.value = categories
        cacheMyCategories(categories)
    }
}