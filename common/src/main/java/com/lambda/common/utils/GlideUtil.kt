package com.lambda.common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.isActive
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


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

    suspend fun download(context: Context, url: String, folder: String) = suspendCoroutine<String> {
        val appName = FileUtil.getFileNameWithSuffix(url)
        Glide.with(context)
            .downloadOnly()
            .load(url)
            .listener(object : RequestListener<File> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<File>,
                    isFirstResource: Boolean
                ): Boolean {
                    if (it.context.isActive) {
                        it.resumeWithException(RuntimeException())
                    }
                    return false
                }

                override fun onResourceReady(
                    resource: File,
                    model: Any,
                    target: Target<File>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    // 复制到目标路径
                    val result = File(folder + appName)
                    File(FileUtil.getFolder(result.path)).mkdirs()
                    FileUtil.copy(resource, result)
                    if (it.context.isActive) {
                        it.resume(result.absolutePath)
                    }
                    return false
                }
            })
            .preload();

    }

    @SuppressLint("CheckResult")
    suspend fun loadBitmap(
        context: Context, url: String, radius: Float = -1f, w: Int = 1,
        h: Int = -1
    ) =
        suspendCancellableCoroutine<Bitmap?> { con ->
            val options = RequestOptions()
            if (w != -1 && h != -1) {
                options.override(w, h)
            } else {
                options.override(300, 300)
            }
            if (radius != -1f) {
                options.transform(RoundedCorners(CommonUtil.dp2px(radius)))
            }
            Glide.with(context).asBitmap().apply(options).load(url)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        if (con.isActive) {
                            con.resume(resource)
                        }
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        if (con.isActive) {
                            con.resume(null)
                        }
                    }

                })
        }

}