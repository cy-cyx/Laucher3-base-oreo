package com.android.launcher3

import android.graphics.Bitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


object ThemeIconMappingV2 {

    private val threadPoolExecutor =
        ThreadPoolExecutor(3, 50, 1, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>(10))

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var cacheBitmap: HashMap<String, Bitmap> = HashMap()

    fun getIconBitmap(): Bitmap {

    }

    /**
     * 获取主题化的icon可能会存在异步加载，异步下先返回占位图
     */
    fun getIconBitmapIfNeedAsyn(bubbleTextView: BubbleTextView): Bitmap {

    }

    private fun transform(): Bitmap {

    }

    // 用于异步加载主题icon
    class TransformIconTask(var bubbleTextView: BubbleTextView) : Runnable {

        var bubbleTextViewWef = WeakReference(bubbleTextView)

        override fun run() {

            val bitmap = transform()

            scope.launch {

            }
        }

    }

}