package com.android.launcher3

import com.android.launcher3.effect.TransitionEffect
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.SpKey
import com.theme.lambda.launcher.utils.getSpFloat
import com.theme.lambda.launcher.utils.getSpInt
import com.theme.lambda.launcher.utils.putSpFloat
import com.theme.lambda.launcher.utils.putSpInt

// 相关调整参数
object AdjustConfig {

    // 动到根本布局时需要重建（彻底的）
    @JvmField
    var needRebuildLauncher = false

    // 动到大小和颜色时需要重绘
    @JvmField
    var needReLoadLauncher = false

    // 切换特效
    @JvmStatic
    fun setEffectId(id: Int) {
        SpKey.keyEffectId.putSpInt(id)
    }

    @JvmStatic
    fun getEffectId(): Int {
        return SpKey.keyEffectId.getSpInt(TransitionEffect.TRANSITION_EFFECT_NONE)
    }

    // 行列数

    @JvmStatic
    fun setColumn(c: Int) {
        if (c != SpKey.keyColumn.getSpInt(LauncherAppState.getInstance(CommonUtil.appContext).invariantDeviceProfile.numColumns)) {
            needRebuildLauncher = true
        }
        SpKey.keyColumn.putSpInt(c)
    }

    @JvmStatic
    fun getColumn(): Int {
        return SpKey.keyColumn.getSpInt(-1)
    }

    @JvmStatic
    fun setRow(r: Int) {
        if (r != SpKey.keyRow.getSpInt(LauncherAppState.getInstance(CommonUtil.appContext).invariantDeviceProfile.numRows)) {
            needRebuildLauncher = true
        }
        SpKey.keyRow.putSpInt(r)
    }

    @JvmStatic
    fun getRow(): Int {
        return SpKey.keyRow.getSpInt(-1)
    }

    // 不同行列下对应的icon大小
    @JvmStatic
    fun getIconSizeByColumn(column: Int): Int {
        return when (column) {
            2 -> 65
            3 -> 60
            4 -> 60
            5 -> 50
            6 -> 40
            else -> 60
        }
    }

    // 不同行列下对应的字体大小
    @JvmStatic
    fun getTextSizeByColumn(column: Int): Float {
        return when (column) {
            2 -> 14.0f
            3 -> 13.0f
            4 -> 13.0f
            5 -> 12.0f
            6 -> 11.0f
            else -> 13.0f
        }
    }

    // icon size

    // 进度条和实际百分对应关系  0 ~ 1 80% ~ 150%
    @JvmStatic
    fun progressToPercent(progress: Float): Float {
        if (progress <= 0.5f) {
            return (0.8 + (0.2 * progress / 0.5f)).toFloat()
        } else {
            return (1f + (0.5 * (progress - 0.5f) / 0.5f)).toFloat()
        }
    }

    @JvmStatic
    fun percentToProgress(progress: Float): Float {
        if (progress <= 1f) {
            return 0.5f - 0.5f * (1f - progress) / 0.2f
        } else {
            return 0.5f + 0.5f * (progress - 1f) / 0.5f
        }
    }

    @JvmStatic
    fun setHomeScreenIconSize(size: Float) {
        SpKey.keyHomeScreenIconSize.putSpFloat(size)
        needReLoadLauncher = true
    }

    @JvmStatic
    fun getHomeScreenIconSize(): Float {
        return SpKey.keyHomeScreenIconSize.getSpFloat(1f)
    }

    @JvmStatic
    fun setAppDrawerIconSize(size: Float) {
        SpKey.keyAppDrawerIconSize.putSpFloat(size)
        needReLoadLauncher = true
    }

    @JvmStatic
    fun getAppDrawerIconSize(): Float {
        return SpKey.keyAppDrawerIconSize.getSpFloat(1f)
    }

    @JvmStatic
    fun setFolderIconSize(size: Float) {
        SpKey.keyFolderIconSize.putSpFloat(size)
        needReLoadLauncher = true
    }

    @JvmStatic
    fun getFolderIconSize(): Float {
        return SpKey.keyFolderIconSize.getSpFloat(1f)
    }
}