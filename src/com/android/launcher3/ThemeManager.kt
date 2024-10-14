package com.android.launcher3

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.theme.lambda.launcher.ad.AdName
import com.theme.lambda.launcher.ad.AdUtil
import com.theme.lambda.launcher.data.model.ManifestBean
import com.theme.lambda.launcher.ui.theme.ThemeActivity
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.FileUtil
import com.theme.lambda.launcher.utils.GsonUtil
import com.theme.lambda.launcher.utils.LauncherUtil
import com.theme.lambda.launcher.utils.ShareUtil
import com.theme.lambda.launcher.utils.SpUtil
import com.theme.lambda.launcher.utils.WallPaperUtil
import com.theme.lambda.launcher.utils.gone
import com.theme.lambda.launcher.utils.visible
import com.theme.lambda.launcher.widget.PreviewControlView
import com.theme.lambda.launcher.widget.WallpaperView
import com.theme.lambda.launcher.widget.dialog.ApplyLauncherPermissionDialog
import com.theme.lambda.launcher.widget.dialog.QuitPreviewSureDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.lang.ref.WeakReference

class ThemeManager {

    private var themeId = "" // 当前的主题id
    private var previewThemeId = "" // 预览的主题id 可能为空
    private var showThemeId = "" // 当前展示主题id，可能是预览

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
                themeId = previewThemeId
                SpUtil.putString(sKeyThemeId, themeId)
                quitPreview()
                setCurShowThemeById(themeId)

                // 判断以下自己是不是默认launcher
                launcher?.let {
                    if (!LauncherUtil.isDefaultLauncher(it)) {
                        ApplyLauncherPermissionDialog(it).apply {
                            clickApplyListen = {
                                LauncherUtil.gotoSetLauncher(it)
                                dismiss()
                            }
                            clickNotNowListen = {
                                dismiss()
                            }
                        }.show()
                    }
                }
            }
        }
    }

    private var firstApplyQuit = true

    fun applyQuitPreviewMode(context: Context) {
        if (firstApplyQuit && !LauncherUtil.isDefaultLauncher(context)) {
            firstApplyQuit = false
            ApplyLauncherPermissionDialog(context).apply {
                clickApplyListen = {
                    dismiss()
                    LauncherUtil.gotoSetLauncher(context)
                }
                clickNotNowListen = {
                    dismiss()
                }
            }.show()
        } else {
            QuitPreviewSureDialog(context).apply {
                onClickContinueListen = {
                    quitPreview()
                    setCurShowThemeById(themeId)
                    // 返回主题选择页,如果是首次还要关一下launcher
                    ThemeActivity.start(context, ThemeActivity.sFromTheme)
                    if (themeId == "") {
                        launcher?.finish()
                    }
                    AdUtil.showAd(AdName.interleaving)
                }
            }.show()
        }
    }

    companion object {

        val sKeyThemeId = "key_theme_id"

        var enterPreviewId = ""

        var themeManagerCache: WeakReference<ThemeManager>? = null

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
                manifestJson,
                ManifestBean::class.java
            )
            return manifest
        }
    }

    fun onCreate() {
        themeManagerCache = WeakReference(this)

        themeId = SpUtil.getString(sKeyThemeId)
        showThemeId = themeId

        // 更新壁纸默认壁纸
        val manifest = getCurManifest()
        if (manifest != null) {
            val wallpaper = getManifestResRootPath() + manifest.background
            wallpaperView?.setPic(wallpaper)
            wallpaperView?.visible()
        }

        // 首次启动在onResume设置，避免二次加载
        if (enterPreviewId != "") {
            previewThemeId = enterPreviewId
            enterPreview()
            enterPreviewId = ""

            setCurShowThemeById(previewThemeId, reload = false)
        }
    }

    fun onStart() {

    }

    fun onResume() {
        if (enterPreviewId != "") {
            previewThemeId = enterPreviewId
            enterPreview()
            enterPreviewId = ""

            // 可能打开着all app需要关闭
            launcher?.closeAllAppLayoutIfNeed()
            setCurShowThemeById(previewThemeId)
        }
    }

    fun onPause() {
        // 如果是默认的launcher的情况下
        launcher?.let {
            if (LauncherUtil.isDefaultLauncher(it) && isPreviewMode){
                QuitPreviewSureDialog(it).apply {
                    onClickContinueListen = {
                        quitPreview()
                        setCurShowThemeById(themeId)
                        // 返回主题选择页,如果是首次还要关一下launcher
                        ThemeActivity.start(context, ThemeActivity.sFromTheme)
                        if (themeId == "") {
                            launcher?.finish()
                        }
                        AdUtil.showAd(AdName.interleaving)
                    }
                }.show()
            }
        }

    }

    fun onStop() {

    }

    fun onDestroy() {

    }

    private fun enterPreview() {
        isPreviewMode = true
        firstApplyQuit = true
        launcher?.runOnUiThread {
            previewControlView?.visibility = View.VISIBLE
        }
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
        ThemeIconMapping.cleanThemeIconCache()
        // 更新桌面
        if (reload) {
            launcher?.reload(true)
        }
        // 更新壁纸
        val manifest = getCurManifest()
        if (manifest != null) {
            val wallpaper = getManifestResRootPath() + manifest.background
            wallpaperView?.setPic(wallpaper)
            wallpaperView?.visible()
        } else {
            wallpaperView?.gone()
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