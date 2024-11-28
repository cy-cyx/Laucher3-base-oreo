package com.android.launcher3

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import android.widget.ImageView
import com.lambda.common.utils.utilcode.util.AppUtils
import com.lambda.common.utils.utilcode.util.ConvertUtils
import com.lambda.common.Constants
import com.lambda.common.utils.CommonUtil
import com.lambda.common.utils.CommonUtil.dp2px
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


object ThemeIconMappingV2 {

    private var iconZoom: Float = 0.6f
    private var whiteFrame: Float = dp2px(3f).toFloat()
    private var roundedCorners: Float = dp2px(10f).toFloat()

    private val threadPoolExecutor =
        ThreadPoolExecutor(3, 50, 1, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>(400))

    private val uiScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var cacheBitmap: HashMap<String, Bitmap> = HashMap()

    // 主题默认底
    private var generalFrameBm: Bitmap? = null

    // 默认的底
    private var defaultFrameBm: Bitmap? = null

    fun cleanThemeIconCache() {
        generalFrameBm = null
        cacheBitmap.clear()
    }


    /**
     * @param url 当为推荐app不为空
     */
    @JvmStatic
    fun getIconBitmap(pm: String, url: String): Bitmap {
        // 使用缓存
        if (cacheBitmap.containsKey(pm)) {
            return cacheBitmap[pm]!!
        }
        val bitmap = transform(pm, url)
        cacheBitmap[pm] = bitmap
        return bitmap
    }

    /**
     * 获取主题化的icon可能会存在异步加载，异步下先返回占位图
     */
    @JvmStatic
    fun getIconBitmapIfNeedAsyn(view: View, pm: String, url: String): Bitmap {
        // 使用缓存
        if (cacheBitmap.containsKey(pm)) {
            return cacheBitmap[pm]!!
        }

        val task = TransformIconTask(
            view,
            pm,
            url,
            ThemeManager.getThemeManagerIfExist()?.showThemeId ?: ""
        )
        threadPoolExecutor.execute(task)

        // todo 加载中后面得换
        return getDefaultIcon()
    }

    private fun getDefaultIcon(): Bitmap {
        if (defaultFrameBm == null) {
            defaultFrameBm = BitmapFactory.decodeResource(
                CommonUtil.appContext!!.resources,
                R.drawable.bg_default_icon
            )
        }
        return defaultFrameBm!!
    }

    private fun transform(packageName: String, url: String): Bitmap {
        // 往后走走看看需不需要换主题

        // 看一下主题用不用替换icon
        val themeManager = ThemeManager.getThemeManagerIfExist()
        if (themeManager != null) {
            val curManifest = themeManager.getCurManifest()
            if (curManifest != null) {
                val icons = curManifest.icons
                for (iconBean in icons) {
                    if (iconBean.pn == packageName) {
                        val iconPath = themeManager.getManifestResRootPath() + iconBean.icon

                        try {
                            val bitmap = BitmapFactory.decodeFile(iconPath)
                            return bitmap
                        } catch (e: Exception) {
                        }
                    }
                }

                // 都没有 看看通用的
                for (iconBean in icons) {
                    if (iconBean.pn == "general_background") {
                        try {
                            var gFm: Bitmap? = null
                            if (generalFrameBm == null) {
                                val iconPath = themeManager.getManifestResRootPath() + iconBean.icon
                                gFm = BitmapFactory.decodeFile(iconPath)
                                generalFrameBm = gFm
                            }

                            val frameBm = generalFrameBm!!.copy(Bitmap.Config.ARGB_8888, true)
                            val appBitmap = getOriginalIcon(packageName, url)
                            return addFrameIcon(frameBm, appBitmap)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }


        // 都没有套一层默认的
        if (defaultFrameBm == null) {
            defaultFrameBm =
                BitmapFactory.decodeResource(
                    CommonUtil.appContext!!.getResources(),
                    R.drawable.bg_default_icon
                )
        }
        val frameBm = defaultFrameBm!!.copy(Bitmap.Config.ARGB_8888, true)
        val appBitmap = getOriginalIcon(packageName, url)
        return addFrameIcon(frameBm, appBitmap)
    }

    // 叠加背景效果
    private fun addFrameIcon(frameBm: Bitmap, appBm: Bitmap): Bitmap {
        val frameWidth = frameBm.width.toFloat()
        val frameHeight = frameBm.height.toFloat()
        val frameCanvas = Canvas(frameBm)

        val width = appBm.width.toFloat()
        val height = appBm.height.toFloat()

        val resRect = Rect(0, 0, width.toInt(), height.toInt())

        // 计算缩放
        var left = 0f
        var top = 0f
        var right = 0f
        var bottom = 0f
        if (width >= height) {
            val iconWidth = frameWidth * iconZoom
            val iconHeight = iconWidth / width * height

            top = (frameHeight - iconHeight) / 2f
            bottom = top + iconHeight
            left = (frameWidth - iconWidth) / 2f
            right = left + iconWidth
        } else {
            val iconHeight = frameHeight * iconZoom
            val iconWidth = iconHeight / height * width

            top = (frameHeight - iconHeight) / 2f
            bottom = top + iconHeight
            left = (frameWidth - iconWidth) / 2f
            right = left + iconWidth
        }

        val dstRect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())


        val whiteFrameRect = RectF(
            left - whiteFrame,
            top - whiteFrame,
            right + whiteFrame,
            bottom + whiteFrame
        )
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.WHITE

        frameCanvas.drawRoundRect(
            whiteFrameRect,
            roundedCorners,
            roundedCorners,
            paint
        )
        frameCanvas.drawBitmap(appBm, resRect, dstRect, paint)

        return frameBm
    }

    /**
     * 通过获得原icon图
     *
     * @param pm 包名
     */
    private fun getOriginalIcon(pm: String, url: String): Bitmap {
        // 主题,all apps 默认
        if (pm == CommonUtil.appContext!!.packageName) {
            return BitmapFactory.decodeResource(
                CommonUtil.appContext!!.resources,
                R.drawable.ic_themes
            )
        } else if (pm == Constants.sAllppAction) {
            return BitmapFactory.decodeResource(
                CommonUtil.appContext!!.resources,
                R.mipmap.all_apps
            )
        } else if (pm.contains(RecommendAppManager.actionHost)) {
            val temp = BitmapFactory.decodeFile(url)
            // 配置的图片需要强制控制一下宽高
            val target = Bitmap.createBitmap(
                LauncherAppState.getInstance(CommonUtil.appContext!!).invariantDeviceProfile.iconBitmapSize,
                LauncherAppState.getInstance(CommonUtil.appContext!!).invariantDeviceProfile.iconBitmapSize,
                Bitmap.Config.ARGB_8888
            )
            val tempCanvas = Canvas(target)
            tempCanvas.drawBitmap(temp, null, Rect(0, 0, target.width, target.height), null)
            return target
        } else {
            return ConvertUtils.drawable2Bitmap(AppUtils.getAppIcon(pm)) ?: getDefaultIcon()
        }
    }

    // 用于异步加载主题icon
    private class TransformIconTask(textView: View, pm: String, url: String, themeId: String) :
        Runnable {

        var bubbleTextViewWef = WeakReference(textView)
        var packageName = pm
        var iconUrl = url
        var loadThemeId = themeId

        override fun run() {

            val bitmap = transform(packageName, iconUrl)

            uiScope.launch {
                // 避免上次主题的资源混乱
                if (loadThemeId != ThemeManager.getThemeManagerIfExist()?.showThemeId) return@launch

                cacheBitmap[packageName] = bitmap
                bubbleTextViewWef.get()?.let {
                    if (it is BubbleTextView) {
                        it.setIcon(bitmap)
                    } else if (it is ImageView) {
                        it.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }
}