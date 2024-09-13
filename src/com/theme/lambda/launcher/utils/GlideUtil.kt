package com.theme.lambda.launcher.utils

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.io.File

object GlideUtil {

    @SuppressLint("CheckResult")
    fun load(
        view: ImageView,
        url: String,
        w: Int = 1,
        h: Int = -1,
        placeholder: Int = -1,
        radius: Float = -1f
    ) {
        val options = RequestOptions()
        if (w != -1 && h != -1) {
            options.override(w, h)
        }
        if (placeholder != -1) {
            options.placeholder(placeholder)
        }
        if (radius != -1f) {
            options.transform(RoundedCorners(CommonUtil.dp2px(radius)))
        }
        if (url.startsWith("/")) {
            Glide.with(view.context).load(Uri.fromFile(File(url))).into(view)
        } else {
            Glide.with(view.context).load(url).apply(options).into(view)
        }
    }
}