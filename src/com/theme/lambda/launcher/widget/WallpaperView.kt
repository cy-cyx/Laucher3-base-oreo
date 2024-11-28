package com.theme.lambda.launcher.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.android.launcher3.databinding.LayoutWallpaperBinding
import com.lambda.common.utils.BitmapUtil
import com.lambda.common.utils.CommonUtil

class WallpaperView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    val view = LayoutWallpaperBinding.inflate(LayoutInflater.from(context), this, true)

    fun setPic(path: String) {
        reLayout()

        view.picWallpaperIv.setImageBitmap(
            BitmapUtil.decode(
                path,
                reqWidth = CommonUtil.getScreenWidth(),
                reqHeight = CommonUtil.getScreenHeight()
            )
        )
    }

    private fun reLayout() {
        val lp = this.layoutParams as FrameLayout.LayoutParams
        lp.topMargin = -CommonUtil.getStatusBarHeight()
        lp.bottomMargin = -CommonUtil.getActionBarHeight()
        requestLayout()
    }
}