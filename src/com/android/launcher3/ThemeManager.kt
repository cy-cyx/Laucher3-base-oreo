package com.android.launcher3

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.theme.lambda.launcher.data.model.ManifestBean
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

    var launcher: Launcher? = null
    private var manifestBean: ManifestBean? = null

    var isPreviewMode = false
        private set

    var wallpaperView: WallpaperView? = null

    fun bindWallpaperView(view: WallpaperView) {
        wallpaperView = view
        val manifest = getCurManifest()
        if (manifest != null) {
            val wallpaper = getManifestResRootPath() + manifest.background
            wallpaperView?.setPic(wallpaper)
            wallpaperView?.visible()
        } else {
            wallpaperView?.gone()
        }
    }

    var previewControlView: PreviewControlView? = null

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

    var firstApplyQuit = true

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
                }
            }.show()
        }
    }

    companion object {

        val sKeyThemeId = "key_theme_id"

        var themeManagerCache: WeakReference<ThemeManager>? = null

        fun getThemeManagerIfExist(): ThemeManager? {
            return themeManagerCache?.get()
        }

        fun setDefaultTHemeId(id: String) {
            SpUtil.putString(sKeyThemeId, id)
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

    // 进入设置预览模式
    fun enterPreviewModeWithId(themeId: String) {
        previewThemeId = themeId
        enterPreview()
    }

    fun onCreate() {
        themeManagerCache = WeakReference(this)

        themeId = SpUtil.getString(sKeyThemeId)
        showThemeId = themeId

        // 更新壁纸默认壁纸
        val manifest = getCurManifest()
        if (manifest != null) {
            val wallpaper = getManifestResRootPath() + manifest.background
            WallPaperUtil.setHomeAndLockScreen(wallpaper)
        }
    }

    fun onStart() {

    }

    fun onResume() {
        if (isPreviewMode) {
            launcher?.closeAllAppLayoutIfNeed()
        }
        if (isPreviewMode && previewThemeId != "" && previewThemeId != showThemeId) {
            // 如果在预览模式下，预览的主题和现在当前的主题不一致 重新加载桌面
            setCurShowThemeById(previewThemeId)
        }
    }

    fun onPause() {

    }

    fun onStop() {

    }

    fun onDestroy() {
        themeManagerCache = null
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

    private fun setCurShowThemeById(id: String) {
        showThemeId = id
        // 更新桌面
        launcher?.reload(true)
        // 更新壁纸
        val manifest = getCurManifest()
        if (manifest != null) {
            val wallpaper = getManifestResRootPath() + manifest.background
            wallpaperView?.setPic(wallpaper)
            wallpaperView?.visible()

            // 顺便设置一下手机背景壁纸
            if (!isPreviewMode) {
                GlobalScope.launch(Dispatchers.IO) {
                    WallPaperUtil.setHomeAndLockScreen(wallpaper)
                }
            }
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