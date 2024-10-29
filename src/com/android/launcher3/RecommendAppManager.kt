package com.android.launcher3

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import com.lambda.common.http.Global
import com.lambda.common.utils.utilcode.util.Utils
import com.lambda.remoteconfig.LambdaRemoteConfig
import com.theme.lambda.launcher.data.model.OfferConfig
import com.theme.lambda.launcher.statistics.EventName
import com.theme.lambda.launcher.statistics.EventUtil
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.GlideUtil
import com.theme.lambda.launcher.utils.GsonUtil
import com.theme.lambda.launcher.utils.SpKey
import com.theme.lambda.launcher.utils.getSpString
import com.theme.lambda.launcher.utils.putSpString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * 推荐应用管理
 *
 * 由于在桌面添加和移动，故首次确定后就不可再变了
 */
object RecommendAppManager {

    var TAG = "RecommendAppManager"

    @JvmStatic
    var actionHost = "RecommendApp:"  // 用来拼包名和intent

    private var initing = false

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val iconDownloadFolder =
        "${CommonUtil.appContext!!.filesDir.absoluteFile}/recommendApp/"

    private var _offerConfig: OfferConfig? = null

    fun getOfferConfig(): OfferConfig? {
        if (_offerConfig == null) {
            val offerConfigSp = SpKey.keyOfferConfig.getSpString()
            if (offerConfigSp.isNotEmpty()) {
                _offerConfig =
                    GsonUtil.gson.fromJson<OfferConfig>(offerConfigSp, OfferConfig::class.java)
            }
        }
        return _offerConfig
    }

    // 主要是将配置下载成本地方便使用
    fun init(context: Context) {
        Log.d(TAG, "init")

        if (initing) return
        initing = true
        scope.launch {
            try {
                // 先拿sp缓存的
                val offerConfigSp = SpKey.keyOfferConfig.getSpString()
                if (offerConfigSp.isNotEmpty()) {
                    _offerConfig =
                        GsonUtil.gson.fromJson<OfferConfig>(offerConfigSp, OfferConfig::class.java)
                    return@launch
                }

                // 没有再拿远程配置的
                val offerConfigString =
                    LambdaRemoteConfig.getInstance(CommonUtil.appContext!!).getString("OfferConfig")
                val config =
                    GsonUtil.gson.fromJson<OfferConfig>(offerConfigString, OfferConfig::class.java)

                config.offers.forEach {
                    it.localIconUrl = GlideUtil.download(context, it.iconUrl, iconDownloadFolder)
                    NewInstallationManager.addNewInstallAppPackName(actionHost + it.id)
                }
                SpKey.keyOfferConfig.putSpString(GsonUtil.gson.toJson(config))
                _offerConfig = config
            } catch (e: Exception) {

            }
        }
    }

    @JvmStatic
    fun isFeaturedFolder(folder: String): Boolean {
        return Utils.getApp().getString(R.string.featured).contentEquals(folder)
    }

    @JvmStatic
    fun addOfferIntoFeaturedFolder(folderInfo: FolderInfo) {
        val offerConfig = getOfferConfig() ?: return
        try {
            offerConfig.offers.forEach {
                val shortcutInfo = ShortcutInfo(AppInfo().apply {
                    intent = Intent(actionHost + it.id).apply {
                        // todo 为了偷鸡class名改成放图片的
                        setComponent(ComponentName(actionHost + it.id, it.localIconUrl))
                    }
                    title = it.name
                })
                shortcutInfo.iconBitmap =
                    ThemeIconMapping.getThemeBitmap(
                        Utils.getApp(),
                        actionHost + it.id,
                        it.localIconUrl
                    );
                shortcutInfo.contentDescription = ""

                folderInfo.add(shortcutInfo, false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    @JvmStatic
    fun addInfoAllAppsList(allAppsList: AllAppsList) {
        val offerConfig = getOfferConfig() ?: return
        try {
            offerConfig.offers.forEach {
                allAppsList.add(AppInfo().apply {
                    intent = Intent(actionHost + it.id).apply {
                        // todo 为了偷鸡class名改成放图片的
                        setComponent(ComponentName(actionHost + it.id, it.localIconUrl))
                    }
                    title = it.name
                    componentName = ComponentName(actionHost + it.id, it.localIconUrl)
                }, null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun isRecommendApp(shortcutInfo: ShortcutInfo): Boolean {
        return shortcutInfo.getIntent().action?.contains(actionHost) ?: false
    }

    @JvmStatic
    fun isRecommendApp(appInfo: AppInfo): Boolean {
        return appInfo.getIntent().action?.contains(actionHost) ?: false
    }

    @JvmStatic
    fun clickRecommendApp(shortcutInfo: ShortcutInfo) {
        val offerConfig = getOfferConfig() ?: return
        val action = shortcutInfo.getIntent().action
        val actionId = action?.replace(actionHost, "") ?: ""

        val offer = offerConfig.offers.find { it.id == actionId }
        offer?.let {
            var url = offer.clickUrl
            url = url.replace("{gaid}", Global.getUid())
            url = url.replace("{package}", CommonUtil.appContext!!.packageName)
            url = url.replace("{placement}", "home_box")
            CommonUtil.openWebView(CommonUtil.appContext!!, url)
            NewInstallationManager.clickApp(actionHost + it.id)
            EventUtil.logEvent(EventName.LOfferClick, Bundle().apply {
                putString("id", it.id)
                putString("placement", "home_box")
            })
        }
    }

    @JvmStatic
    fun clickRecommendApp(appInfo: AppInfo) {
        val offerConfig = getOfferConfig() ?: return
        val action = appInfo.getIntent().action
        val actionId = action?.replace(actionHost, "") ?: ""

        val offer = offerConfig.offers.find { it.id == actionId }
        offer?.let {
            var url = offer.clickUrl
            url = url.replace("{gaid}", Global.getUid())
            url = url.replace("{package}", CommonUtil.appContext!!.packageName)
            url = url.replace("{placement}", "app_list_icon")
            CommonUtil.openWebView(CommonUtil.appContext!!, url)
            NewInstallationManager.clickApp(actionHost + it.id)
            EventUtil.logEvent(EventName.LOfferClick, Bundle().apply {
                putString("id", it.id)
                putString("placement", "app_list_icon")
            })
        }
    }
}