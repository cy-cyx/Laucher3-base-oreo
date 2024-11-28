package com.android.launcher3

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustEvent
import com.theme.lambda.launcher.ad.AdName
import com.theme.lambda.launcher.ad.AdUtil
import com.theme.lambda.launcher.appwidget.WidgetManager
import com.theme.lambda.launcher.data.model.ManifestBean
import com.theme.lambda.launcher.statistics.EventName
import com.theme.lambda.launcher.statistics.EventUtil.logEvent
import com.theme.lambda.launcher.statistics.FirebaseAnalyticsUtil
import com.theme.lambda.launcher.ui.seticon.SetIconActivity
import com.theme.lambda.launcher.ui.theme.ThemeActivity
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.FileUtil
import com.theme.lambda.launcher.utils.GsonUtil
import com.theme.lambda.launcher.utils.LauncherUtil
import com.theme.lambda.launcher.utils.SpKey
import com.theme.lambda.launcher.utils.SpUtil
import com.theme.lambda.launcher.utils.SystemUtil
import com.theme.lambda.launcher.utils.WallPaperUtil
import com.theme.lambda.launcher.utils.getSpString
import com.theme.lambda.launcher.utils.gone
import com.theme.lambda.launcher.utils.putSpString
import com.theme.lambda.launcher.utils.visible
import com.theme.lambda.launcher.widget.PreviewControlView
import com.theme.lambda.launcher.widget.WallpaperView
import com.theme.lambda.launcher.widget.dialog.ApplyLauncherPermissionDialog
import com.theme.lambda.launcher.widget.dialog.QuitPreviewSureDialog
import com.theme.lambda.launcher.widget.dialog.SetDefaultFailedDialog
import java.io.File
import java.lang.ref.WeakReference

class ThemeManager {

    var themeId = "" // 当前的主题id
    var previewThemeId = "" // 预览的主题id 可能为空
    var showThemeId = "" // 当前展示主题id，可能是预览

    private var curManifestId = "" // 当前配置文件的主题id
    private var manifestBean: ManifestBean? = null

    var launcher: Launcher? = null

    var isPreviewMode = false
        private set

    private var wallpaperView: WallpaperView? = null

    fun bindWallpaperView(view: WallpaperView) {
        wallpaperView = view
        wallpaperView?.gone()
    }

    private var previewControlView: PreviewControlView? = null

    fun bindPreviewControlView(view: PreviewControlView) {
        previewControlView = view

        previewControlView?.controlListen = object : PreviewControlView.ControlListen {
            override fun onCancel() {
                launcher?.let {
                    applyQuitPreviewMode(it)
                }
            }

            override fun onSet() {
                set()
            }

            override fun setIcon() {
                launcher?.let {
                    SetIconActivity.start(it, previewThemeId)
                }
            }
        }
    }

    private var quitPreviewSureDialog: QuitPreviewSureDialog? = null

    fun applyQuitPreviewMode(context: Context) {
        if (quitPreviewSureDialog == null) {
            quitPreviewSureDialog = QuitPreviewSureDialog(context).apply {
                onClickContinueListen = {
                    set()
                }
                onClickQuitListen = {
                    quit(context)
                }
            }
        }
        quitPreviewSureDialog?.show()
    }

    companion object {

        val sKeyThemeId = "key_theme_id"

        var enterPreviewId = ""

        var themeManagerCache: WeakReference<ThemeManager>? = null

        var clickSet = false

        fun getThemeManagerIfExist(): ThemeManager? {
            return themeManagerCache?.get()
        }

        private fun decodeManifest(id: String): ManifestBean? {
            // 解析对应配置
            val filesDir: String = CommonUtil.appContext!!.getFilesDir().getPath()
            val manifestFile = File("$filesDir/wallpaper/$id/manifest.json")
            if (!manifestFile.exists()) return null;

            val manifestJson: String = FileUtil.readStringFromFile(manifestFile)
            val manifest: ManifestBean = GsonUtil.gson.fromJson(
                manifestJson, ManifestBean::class.java
            )
            return manifest
        }
    }

    private fun set() {
        launcher?.let {
            if (!LauncherUtil.isDefaultLauncher(it)) {
                ApplyLauncherPermissionDialog(it).apply {
                    from = ApplyLauncherPermissionDialog.sFromDetail
                    clickApplyListen = {
                        LauncherUtil.gotoSetLauncher(it)
                        logEvent(EventName.permissionDialog2Show, Bundle().apply {
                            putString("scene", ApplyLauncherPermissionDialog.sFromDetail)
                            putString("permission", "launcher")
                        })
                        // 避免重建回来所选主题丢失
                        SpUtil.putString(sKeyThemeId, previewThemeId)
                        dismiss()

                        clickSet = true
                    }
                    clickNotNowListen = {
                        dismiss()
                    }
                }.show()
            } else {
                themeId = previewThemeId
                SpUtil.putString(sKeyThemeId, themeId)
                quitPreview()
                setCurShowThemeById(themeId)

                logEvent(EventName.setSuccess, Bundle().apply {
                    putString("id", previewThemeId)
                })
            }
        }
        logEvent(EventName.setClick, Bundle().apply {
            putString("id", previewThemeId)
        })
    }

    private fun quit(context: Context) {
        quitPreview()
        setCurShowThemeById(themeId)
        // 返回主题选择页,如果是首次还要关一下launcher
        ThemeActivity.start(context, ThemeActivity.sFromTheme)
        if (themeId == "") {
            launcher?.finish()
        }
        AdUtil.showAd(AdName.interleaving)
    }

    fun onCreate() {
        themeManagerCache = WeakReference(this)

        themeId = SpUtil.getString(sKeyThemeId)
        showThemeId = themeId

        // 判断一下是否要进入预览
        if (enterPreviewId != "") {
            previewThemeId = enterPreviewId
            enterPreview()
            enterPreviewId = ""
            // 首次启动 onResume走加载逻辑，这里就无需手动加载 避免二次加载
            setCurShowThemeById(previewThemeId, reload = false)
        }

        // 由于重建故得这么统计
        if (clickSet) {
            logEvent(EventName.setSuccess, Bundle().apply {
                putString("id", themeId)
            })
            clickSet = false
        }
    }

    // 多Laucher下的重建问题
    fun reLoad() {
        themeId = SpUtil.getString(sKeyThemeId)
        showThemeId = themeId

        // 更新壁纸默认壁纸
        setWallpaper()
    }

    fun onStart() {

    }

    fun onResume() {
        // 解决多launcher问题
        themeManagerCache = WeakReference(this)

        if (enterPreviewId != "") {
            previewThemeId = enterPreviewId
            enterPreview()
            enterPreviewId = ""

            // 可能打开着all app需要关闭
            launcher?.closeAllAppLayoutIfNeed()
            setCurShowThemeById(previewThemeId)
        } else {
            // 如果不是预览就保证设置正确的壁纸
            setWallpaper()
        }

        launcher?.let {
            if (LauncherUtil.isDefaultLauncher(it)) {
                logEvent(EventName.activate, Bundle())
                FirebaseAnalyticsUtil.logEvent(EventName.activate, Bundle())
                Adjust.trackEvent(AdjustEvent("wbnbw3"))
            }
            if (LauncherUtil.gotoSetting) {
                if (LauncherUtil.isDefaultLauncher(it)) {
                    logEvent(EventName.permissionGrant, Bundle().apply {
                        putString("scene", "detail")
                        putString("permission", "launcher")
                    })
                } else {
                    // 设置回来失败
                    SetDefaultFailedDialog(it).show()
                }
            }
        }
        LauncherUtil.gotoSetting = false
    }

    fun onPause() {
    }

    fun onStop() {
        // 找个时机偷换用户真正的壁纸
        launcher?.let {
            if (isPreviewMode) return
            if (LauncherUtil.isDefaultLauncher(it)) {
                // 华为安卓8 会报设置线程没有内存权限
                if ((SystemUtil.getDeviceBrand() == SystemUtil.PHONE_HUAWEI || SystemUtil.getDeviceBrand() == SystemUtil.PHONE_HONOR)
                    && Build.VERSION.SDK_INT < Build.VERSION_CODES.P
                ) {
                    return@let
                }
                if (SpKey.curUserWallpaperId.getSpString() != themeId) {
                    val manifest = getCurManifest()
                    if (manifest != null) {
                        val wallpaper = getManifestResRootPath() + manifest.background
                        WallPaperUtil.setHomeAndLockScreen(wallpaper)
                    }
                    SpKey.curUserWallpaperId.putSpString(themeId)
                }
            }
        }
    }

    fun onDestroy() {

    }

    // 处理重建返回状态不见了的问题
    fun onSaveInstanceState() {
        if (!LauncherUtil.gotoSetting && isPreviewMode) {
            enterPreviewId = showThemeId
        }
    }

    private fun enterPreview() {
        isPreviewMode = true
        launcher?.runOnUiThread {
            previewControlView?.visibility = View.VISIBLE
            previewControlView?.reLayout()
        }
        logEvent(EventName.detailPageView, Bundle().apply {
            putString("id", previewThemeId)
        })
    }

    private fun quitPreview() {
        isPreviewMode = false
        launcher?.runOnUiThread {
            previewControlView?.visibility = View.GONE
        }
    }

    private fun setCurShowThemeById(id: String, reload: Boolean = true) {
        if (showThemeId == id) return
        showThemeId = id
        ThemeIconMappingV2.cleanThemeIconCache()
        // 更新桌面
        if (reload) {
            launcher?.reload(true)
        }
        // 更新壁纸
        setWallpaper()

        // 更新相关组件
        WidgetManager.upData()
    }

    /**
     * 由于部分机型设置壁纸会重构launcher导致启动卡顿，故使用真假壁纸交替使用
     */
    private fun setWallpaper() {
        launcher?.let {
            if (isPreviewMode || !LauncherUtil.isDefaultLauncher(it)) {
                val manifest = getCurManifest()
                if (manifest != null) {
                    val wallpaper = getManifestResRootPath() + manifest.background
                    wallpaperView?.setPic(wallpaper)
                    wallpaperView?.visible()
                } else {
                    wallpaperView?.gone()
                }
            } else {
                // 如果已经找机会设置真壁纸了，就不用再显示假壁纸，这样用户想换也能换了
                if (SpKey.curUserWallpaperId.getSpString() == themeId) {
                    wallpaperView?.gone()
                } else {
                    val manifest = getCurManifest()
                    if (manifest != null) {
                        val wallpaper = getManifestResRootPath() + manifest.background
                        wallpaperView?.setPic(wallpaper)
                        wallpaperView?.visible()
                    } else {
                        wallpaperView?.gone()
                    }
                }
            }
        }
    }

    fun getCurManifest(): ManifestBean? {
        var result: ManifestBean? = null
        if (manifestBean == null) {
            if (showThemeId != "") {
                result = decodeManifest(showThemeId)
                curManifestId = showThemeId
            }
        } else {
            if (showThemeId != curManifestId) {
                result = decodeManifest(showThemeId)
                curManifestId = showThemeId
            }
        }
        return result
    }

    fun getManifestResRootPath(): String {
        val filesDir: String = CommonUtil.appContext!!.filesDir.path
        return "$filesDir/wallpaper/$curManifestId"
    }
}