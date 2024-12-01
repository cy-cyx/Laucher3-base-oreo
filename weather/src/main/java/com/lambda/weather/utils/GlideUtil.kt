package com.lambdaweather.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory


object GlideUtil {
    private const val mRadius20 = 20
    const val mRadius40 = 40
    fun getRoundRequestOptions(radius: Int): RequestOptions {
        if (radius == 0) {
            return RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)//磁盘缓存
                .skipMemoryCache(false)//不做内存缓存
        }
        return RequestOptions()
            .transform(RoundedCorners(radius))
            .diskCacheStrategy(DiskCacheStrategy.ALL)//磁盘缓存
            .skipMemoryCache(false)//不做内存缓存
    }

    private fun getCircleRequestOptions(): RequestOptions {
        val roundedCorners = CircleCrop()
        return RequestOptions()
            .optionalTransform(roundedCorners)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(true)
    }

    fun loadUrlImage(context: Context, url: String, imageView: ImageView, radius: Int = mRadius20) {
        loadUrlImage(context, url, imageView, null, radius)
    }

    fun loadUrlImage(
        context: Fragment,
        url: String,
        imageView: ImageView,
        radius: Int = mRadius20,
        ph: Int?
    ) {
        Glide.with(context)
            .syncWithLifecycleOwner(imageView)
            .load(url)
            .apply(getRoundRequestOptions(radius))
            .placeholder(ph ?: 0)
            .override(imageView.width, imageView.height)
            .into(imageView)
    }

    fun loadUrlImage(
        context: Fragment,
        url: String,
        imageView: ImageView,
        ph: Int?
    ) {
        if (context.activity != null && context.isAdded) {
            Glide.with(context)
                .syncWithLifecycleOwner(imageView)
                .load(url)
                .apply(getRoundRequestOptions(mRadius20))
                .placeholder(ph ?: 0)
                .override(imageView.width, imageView.height)
                .thumbnail(0.1f)
                .into(imageView)
        }
    }

    fun loadUrlImage(context: Context, url: String, imageView: ImageView, ph: Int?) {
        val drawableCrossFadeFactory =
            DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
        Glide.with(context)
            .syncWithLifecycleOwner(imageView)
            .load(url)
            .apply(getRoundRequestOptions(mRadius20))
            .transition(DrawableTransitionOptions.withCrossFade(drawableCrossFadeFactory))
            .override(imageView.width, imageView.height)
            .into(imageView)
    }

    fun loadResImage(
        context: Context,
        res: Int,
        imageView: ImageView,
        radius: Int = mRadius20,
        ph: Int? = null
    ) {
        Glide.with(context)
            .load(res)
            .apply(getRoundRequestOptions(radius))
            .placeholder(ph ?: 0)
            .override(imageView.width, imageView.height)
            .into(imageView)
    }

    fun loadUrlImagePreLoad(context: Fragment, url: String) {
        Glide.with(context)
            .load(url)
            .preload()
    }

    fun loadUrlImage(
        context: Context,
        url: String,
        imageView: ImageView,
        ph: Int?,
        radius: Int = mRadius20
    ) {
        if (context is Activity) {
            if (context.isDestroyed) return
        }

        Glide.with(context)
            .load(url)
            .apply(getCircleRequestOptions())
            .placeholder(ph?:0)
            .override(imageView.width, imageView.height)
            .into(imageView)
    }


    private fun RequestManager.syncWithLifecycleOwner(view: View): RequestManager {

        val syncRequest = object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) = onStart()
            override fun onStop(owner: LifecycleOwner) = onStop()
            override fun onDestroy(owner: LifecycleOwner) {
                clear(view)
                owner.lifecycle.removeObserver(this)
            }
        }

        view.findViewTreeLifecycleOwner()?.lifecycle?.addObserver(syncRequest)

        return this
    }



    fun trimMemory(context: Context?, level: Int) {
        Glide.get(context!!).trimMemory(level)
    }

    fun pauseImage(context: Context) {
        Glide.with(context).pauseRequests()
    }

    fun resumeImage(context: Context) {
        Glide.with(context).resumeRequests()
    }
}
