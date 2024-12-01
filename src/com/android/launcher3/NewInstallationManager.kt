package com.android.launcher3

import com.google.gson.reflect.TypeToken
import com.lambda.common.utils.GsonUtil
import com.lambda.common.utils.SpKey
import com.lambda.common.utils.SpUtil
import com.lambda.common.utils.getSpBool
import com.lambda.common.utils.putSpBool
import com.lambda.common.utils.putSpString
import java.lang.ref.WeakReference

object NewInstallationManager {

    private val listens = ArrayList<WeakReference<NewInstallationClickUpDataListen>>()

    private val newInstallAppList: ArrayList<String> by lazy {
        val result = ArrayList<String>()
        val appListString = SpUtil.getString(SpKey.keyNewInstallAppList)
        if (appListString.isNotBlank()) {
            val typeToken = object : TypeToken<List<String>>() {}
            result.addAll(GsonUtil.gson.fromJson(appListString, typeToken).toMutableList())
        }
        // 添加一些默认的(如内置引用)
        if (!SpKey.keyDefaultNewInstall.getSpBool(false)) {
            SpKey.keyDefaultNewInstall.putSpBool(true)

            result.add(InnerAppManager.InnerNewsAction)

            SpKey.keyNewInstallAppList.putSpString(GsonUtil.gson.toJson(result))
        }

        result
    }


    fun addNewInstallAppPackName(appPackName: String) {
        if (!newInstallAppList.contains(appPackName)) {
            newInstallAppList.add(appPackName)
            SpUtil.putString(SpKey.keyNewInstallAppList, GsonUtil.gson.toJson(newInstallAppList))
        }
    }

    fun isNewInstallApp(info: ItemInfo): Boolean {
        val pkg = info.intent?.component?.packageName ?: ""
        return newInstallAppList.contains(pkg)
    }

    fun bindListen(listen: NewInstallationClickUpDataListen) {
        listens.add(WeakReference(listen))
        removeListenIsRelease()
    }

    fun clickApp(appPackName: String) {
        if (newInstallAppList.contains(appPackName)) {
            newInstallAppList.remove(appPackName)
            SpUtil.putString(SpKey.keyNewInstallAppList, GsonUtil.gson.toJson(newInstallAppList))

            listens.forEach {
                it.get()?.onUpData()
            }
        }
    }

    private fun removeListenIsRelease() {
        listens.removeIf { it.get() == null }
    }

    interface NewInstallationClickUpDataListen {

        fun onUpData()
    }
}