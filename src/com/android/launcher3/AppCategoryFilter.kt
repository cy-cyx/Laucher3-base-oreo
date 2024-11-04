package com.android.launcher3

import android.content.pm.ApplicationInfo
import android.content.pm.LauncherActivityInfo
import android.os.Build
import android.os.UserHandle
import com.android.launcher3.compat.LauncherAppsCompat
import com.android.launcher3.compat.UserManagerCompat
import com.lambda.common.utils.utilcode.util.GsonUtils
import com.lambda.common.utils.utilcode.util.ResourceUtils
import com.lambda.common.utils.utilcode.util.Utils
import com.theme.lambda.launcher.data.model.Package

object AppCategoryFilter {
    var appInfoList = mutableListOf<AppInfo>()
    private val storeList: List<String> by lazy {
        GsonUtils.fromJson<List<Package>>(
            ResourceUtils.readAssets2String("store.json"),
            GsonUtils.getListType(Package::class.java)
        ).map { it.packageX ?: "" }
    }
    private val gamesList: List<String> by lazy {
        GsonUtils.fromJson<List<Package>>(
            ResourceUtils.readAssets2String("games.json"),
            GsonUtils.getListType(Package::class.java)
        ).map { it.packageX ?: "" }
    }
    private val utilitiesList: List<String> by lazy {
        GsonUtils.fromJson<List<Package>>(
            ResourceUtils.readAssets2String("utilities.json"),
            GsonUtils.getListType(Package::class.java)
        ).map { it.packageX ?: "" }
    }
    private val artDesignList: List<String> by lazy {
        GsonUtils.fromJson<List<Package>>(
            ResourceUtils.readAssets2String("art_design.json"),
            GsonUtils.getListType(Package::class.java)
        ).map { it.packageX ?: "" }
    }
    private val healthList: List<String> by lazy {
        GsonUtils.fromJson<List<Package>>(
            ResourceUtils.readAssets2String("health.json"),
            GsonUtils.getListType(Package::class.java)
        ).map { it.packageX ?: "" }
    }
    private val socialList: List<String> by lazy {
        GsonUtils.fromJson<List<Package>>(
            ResourceUtils.readAssets2String("social.json"),
            GsonUtils.getListType(Package::class.java)
        ).map { it.packageX ?: "" }
    }
    private val musicVideoList: List<String> by lazy {
        GsonUtils.fromJson<List<Package>>(
            ResourceUtils.readAssets2String("music_video.json"),
            GsonUtils.getListType(Package::class.java)
        ).map { it.packageX ?: "" }
    }
    private val shoppingList: List<String> by lazy {
        GsonUtils.fromJson<List<Package>>(
            ResourceUtils.readAssets2String("shopping.json"),
            GsonUtils.getListType(Package::class.java)
        ).map { it.packageX ?: "" }
    }

    @JvmField
    val customFoldersName = arrayListOf(
        Utils.getApp().getString(R.string.games),
        Utils.getApp().getString(R.string.utilities),
        Utils.getApp().getString(R.string.art_design),
        Utils.getApp().getString(R.string.health),
        Utils.getApp().getString(R.string.social),
        Utils.getApp().getString(R.string.shopping),
        Utils.getApp().getString(R.string.music_video)
    )

    @JvmStatic
    fun getAppInfoList(
        launcherApps: LauncherAppsCompat,
        profiles: List<UserHandle>,
        userManager: UserManagerCompat
    ): List<AppInfo> {
        if (appInfoList.isEmpty()) {
            for (user in profiles) {
                val apps: List<LauncherActivityInfo> = launcherApps.getActivityListCache(user)
                for (app in apps) {
                    appInfoList.add(AppInfo(app, user, userManager.isQuietModeEnabled(user)))
                }
            }
        }
        return appInfoList
    }

    @JvmStatic
    fun filter(packageName: String, category: CharSequence): Boolean {
        val pm = Utils.getApp().packageManager
        var c = -1
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                c = pm.getApplicationInfo(packageName, 0).category
            }
        } catch (ignored: Exception) {
        }
        if (Utils.getApp().getString(R.string.store).contentEquals(category)) {
            return storeList.contains(packageName)
        } else if (Utils.getApp().getString(R.string.games).contentEquals(category)) {
            return ApplicationInfo.CATEGORY_GAME == c || gamesList.contains(packageName)
        } else if (Utils.getApp().getString(R.string.utilities).contentEquals(category)) {
            return ApplicationInfo.CATEGORY_NEWS == c || ApplicationInfo.CATEGORY_MAPS == c
                    || ApplicationInfo.CATEGORY_PRODUCTIVITY == c || ApplicationInfo.CATEGORY_ACCESSIBILITY == c
                    || utilitiesList.contains(packageName)
        } else if (Utils.getApp().getString(R.string.art_design).contentEquals(category)) {
            return ApplicationInfo.CATEGORY_IMAGE == c || artDesignList.contains(packageName)
        } else if (Utils.getApp().getString(R.string.health).contentEquals(category)) {
            return healthList.contains(packageName) || healthList.contains(packageName)
        } else if (Utils.getApp().getString(R.string.social).contentEquals(category)) {
            return ApplicationInfo.CATEGORY_SOCIAL == c || socialList.contains(packageName)
        } else if (Utils.getApp().getString(R.string.shopping).contentEquals(category)) {
            return shoppingList.contains(packageName)
        } else if (Utils.getApp().getString(R.string.music_video).contentEquals(category)) {
            return ApplicationInfo.CATEGORY_AUDIO == c || ApplicationInfo.CATEGORY_VIDEO == c || musicVideoList.contains(
                packageName
            )
        }
        return false
    }
}
