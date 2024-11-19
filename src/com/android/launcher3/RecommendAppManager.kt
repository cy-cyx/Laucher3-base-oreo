package com.android.launcher3

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.gson.reflect.TypeToken
import com.lambda.common.http.Global
import com.lambda.common.utils.utilcode.util.Utils
import com.lambda.remoteconfig.LambdaRemoteConfig
import com.theme.lambda.launcher.data.model.OfferConfig
import com.theme.lambda.launcher.data.model.Offers
import com.theme.lambda.launcher.statistics.EventName
import com.theme.lambda.launcher.statistics.EventUtil
import com.theme.lambda.launcher.utils.AppUtil
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.GlideUtil
import com.theme.lambda.launcher.utils.GsonUtil
import com.theme.lambda.launcher.utils.SpKey
import com.theme.lambda.launcher.utils.getSpInt
import com.theme.lambda.launcher.utils.getSpString
import com.theme.lambda.launcher.utils.putSpInt
import com.theme.lambda.launcher.utils.putSpLong
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

    // 在下次Launcher OnResume时判断是否需要更新桌面
    private var needUpDataRecommend = false

    @JvmStatic
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

    // 已经移除的id
    private val removeOfferIds: ArrayList<String> by lazy {
        val result = arrayListOf<String>()
        try {
            val keyRemoveOfferIdsSp = SpKey.keyRemoveOfferId.getSpString()
            val typeToken = object : TypeToken<List<String>>() {}
            result.addAll(ArrayList(GsonUtil.gson.fromJson(keyRemoveOfferIdsSp, typeToken)))
        } catch (e: Exception) {
        }
        result
    }

    // 记录已经红点更新的推荐ids 避免重复提醒
    private val newIdsList: ArrayList<String> by lazy {
        val result = ArrayList<String>()
        val newIds = SpKey.keyRecommendNewIds.getSpString()
        if (newIds.isNotBlank()) {
            val typeToken = object : TypeToken<List<String>>() {}
            result.addAll(GsonUtil.gson.fromJson(newIds, typeToken).toMutableList())
        }
        result
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

                // 需要先把图标下载到本地
                config.offers.forEach {
                    it.localIconUrl = GlideUtil.download(context, it.iconUrl, iconDownloadFolder)
                }
                // 新安装红点
                config.offers.forEach {
                    if (!newIdsList.contains(it.id)) {
                        NewInstallationManager.addNewInstallAppPackName(actionHost + it.id)
                        newIdsList.add(it.id)
                    }
                }
                SpKey.keyRecommendNewIds.putSpString(GsonUtil.gson.toJson(newIdsList))
                SpKey.keyOfferConfig.putSpString(GsonUtil.gson.toJson(config))
                _offerConfig = config
                SpKey.keyRecommendHashcode.putSpInt(config.hashCode())
            } catch (e: Exception) {

            }
        }
    }

    // 1个小时检查一次
    private var lastUpDataTime = 0L
    private var upDataInterval = if (BuildConfig.isDebug) 60 * 1000 else 24 * 60 * 60 * 1000

    @JvmStatic
    fun upDataRecommendAppManagerIfNeed(): Boolean {
        // 判断是否异步去判断,下次onResume刷新
        if (System.currentTimeMillis() - lastUpDataTime > upDataInterval) {
            checkNeedUpData()
        }

        // 通过标志位
        if (needUpDataRecommend) {
            needUpDataRecommend = false
            return true
        } else {
            return false
        }
    }

    private fun checkNeedUpData() {
        scope.launch {
            try {
                val offerConfigString =
                    LambdaRemoteConfig.getInstance(CommonUtil.appContext!!).getString("OfferConfig")
                val config =
                    GsonUtil.gson.fromJson<OfferConfig>(offerConfigString, OfferConfig::class.java)

                // 判断json hashcode
                if (SpKey.keyRecommendHashcode.getSpInt() != config.hashCode()) {

                    config.offers.forEach {
                        it.localIconUrl = GlideUtil.download(
                            CommonUtil.appContext!!,
                            it.iconUrl,
                            iconDownloadFolder
                        )
                    }

                    config.offers.forEach {
                        // 处理红点
                        if (!newIdsList.contains(it.id)) {
                            NewInstallationManager.addNewInstallAppPackName(actionHost + it.id)
                            newIdsList.add(it.id)
                        }
                    }
                    SpKey.keyRecommendNewIds.putSpString(GsonUtil.gson.toJson(newIdsList))
                    SpKey.keyOfferConfig.putSpString(GsonUtil.gson.toJson(config))

                    _offerConfig = config
                    SpKey.keyRecommendHashcode.putSpInt(config.hashCode())

                    // 设置下次更新标识位
                    needUpDataRecommend = true
                }
            } catch (e: Exception) {
            }
        }
    }

    @JvmStatic
    fun addInfoAllAppsList(allAppsList: AllAppsList) {
        val offerConfig = getOfferConfig() ?: return
        try {
            offerConfig.offers.forEach {

                // 判断是否已经安装，或者已被移除
                if (isCanAdd(it, true)) {
                    allAppsList.add(AppInfo().apply {
                        intent = Intent(actionHost + it.id).apply {
                            // todo 为了偷鸡class名改成放图片的
                            setComponent(ComponentName(actionHost + it.id, it.localIconUrl))
                        }
                        title = it.name
                        componentName = ComponentName(actionHost + it.id, it.localIconUrl)
                    }, null)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun isCanAdd(offer: Offers, filterRemove: Boolean): Boolean {
        if (AppUtil.checkAppInstalled(CommonUtil.appContext, offer.pn)) {
            return false
        }
        if (removeOfferIds.contains(offer.id) && filterRemove) {
            return false
        }
        return true
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
        clickRecommendApp(offer)
    }

    @JvmStatic
    fun clickRecommendApp(appInfo: AppInfo) {
        val offerConfig = getOfferConfig() ?: return
        val action = appInfo.getIntent().action
        val actionId = action?.replace(actionHost, "") ?: ""
        val offer = offerConfig.offers.find { it.id == actionId }
        clickRecommendApp(offer)
    }

    fun clickRecommendApp(offer: Offers?) {
        offer?.let {
            // 如果安装了直接打开
            if (AppUtil.checkAppInstalled(CommonUtil.appContext!!, offer.pn)) {
                AppUtil.openAppByName(CommonUtil.appContext!!, offer.pn)
            } else {
                var url = offer.clickUrl
                url = url.replace("{gaid}", Global.getUid())
                url = url.replace("{package}", CommonUtil.appContext!!.packageName)
                url = url.replace("{placement}", "app_list_icon")
                CommonUtil.openWebView(CommonUtil.appContext!!, url)
            }
            NewInstallationManager.clickApp(actionHost + it.id)
            EventUtil.logEvent(EventName.LOfferClick, Bundle().apply {
                putString("id", it.id)
                putString("placement", "app_list_icon")
            })

            // 判断是否超过最大点击
            if (offer.maxClick > 0 && !removeOfferIds.contains(offer.id)) {
                val key = "${SpKey.keyOfferClickTime}${offer.id}"
                var time = key.getSpInt(0)
                time++
                key.putSpInt(time)

                if (time >= offer.maxClick) {
                    needUpDataRecommend = true

                    // 当作它已经移除
                    if (!removeOfferIds.contains(offer.id)) {
                        removeOfferIds.add(offer.id)
                        SpKey.keyRemoveOfferId.putSpString(GsonUtil.gson.toJson(removeOfferIds))
                    }
                }
            }
        }
    }

    @JvmStatic
    fun isCanRemove(packName: String): Boolean {
        val offerConfig = getOfferConfig() ?: return true
        val actionId = packName.replace(actionHost, "")
        val offer = offerConfig.offers.find { it.id == actionId }
        return offer?.isRemovable ?: true
    }

    @JvmStatic
    fun remove(appInfo: AppInfo) {
        val packName = appInfo.intent?.action ?: ""
        val actionId = packName.replace(actionHost, "")

        if (!removeOfferIds.contains(actionId)) {
            removeOfferIds.add(actionId)
            SpKey.keyRemoveOfferId.putSpString(GsonUtil.gson.toJson(removeOfferIds))
        }
    }

    @JvmStatic
    fun dealRemoveRecomAppOnReallyInstall(
        launcher: Launcher,
        installApp: AppInfo,
        allApps: List<AppInfo>
    ) {
        val offerConfig = getOfferConfig() ?: return
        val packageName = installApp.componentName?.packageName ?: ""
        val offer = offerConfig.offers.find { it.pn.equals(packageName) }
        offer?.let { offer ->
            val appInfo =
                allApps.find {
                    it.componentName?.packageName?.equals("${actionHost}${offer.id}") ?: false
                }
            appInfo?.let { info ->
                launcher.removeAppInfoFormAppView(arrayListOf(info))
                remove(info)
                EventUtil.logEvent(EventName.LOfferExists, Bundle().apply {
                    putString("id", offer.id)
                })
            }
        }
    }

    fun getYourMayLikeOffers(): ArrayList<Offers> {
        val result = ArrayList<Offers>()
        val offerConfig = getOfferConfig()
        offerConfig?.offers?.forEach {
            // 判断是否已经安装
            if (!AppUtil.checkAppInstalled(CommonUtil.appContext, it.pn)) {
                result.add(it)
            }
        }
        return result
    }

    @JvmStatic
    fun addOfferIntoFeaturedFolder(folderInfo: FolderInfo) {
        val offerConfig = getOfferConfig() ?: return
        try {
            offerConfig.offers.forEach {
                if (isCanAdd(it, false)) {
                    val shortcutInfo = ShortcutInfo(AppInfo().apply {
                        intent = Intent(actionHost + it.id).apply {
                            // todo 为了偷鸡class名改成放图片的
                            setComponent(ComponentName(actionHost + it.id, it.localIconUrl))
                        }
                        title = it.name
                    })
                    shortcutInfo.iconBitmap =
                        ThemeIconMappingV2.getIconBitmap(
                            actionHost + it.id,
                            it.localIconUrl
                        )
                    shortcutInfo.contentDescription = ""
                    folderInfo.add(shortcutInfo, false)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun isFeaturedFolder(folder: String): Boolean {
        return Utils.getApp().getString(R.string.featured).contentEquals(folder)
    }
}