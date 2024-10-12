package com.theme.lambda.launcher.widget

import android.content.Context
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.android.launcher3.databinding.LayoutWallpaperBinding
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.marginStatusBarHeight

class WallpaperView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    companion object {
        const val sPic = 1 // 用于后续处理复杂的壁纸
    }

    val view = LayoutWallpaperBinding.inflate(LayoutInflater.from(context), this, true)

    fun setPic(path: String) {
        reset()

        val bitmap = BitmapFactory.decodeFile(path)
        view.picWallpaperIv.setImageBitmap(bitmap)
    }

    private fun reset() {
        val lp = this.layoutParams as FrameLayout.LayoutParams
        lp.topMargin = -CommonUtil.getStatusBarHeight()
        lp.bottomMargin = -CommonUtil.getActionBarHeight()
        requestLayout()
    }
}