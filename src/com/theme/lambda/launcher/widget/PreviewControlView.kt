package com.theme.lambda.launcher.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.android.launcher3.databinding.LayoutPreviewControlBinding

class PreviewControlView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    init {
        LayoutPreviewControlBinding.inflate(LayoutInflater.from(context), this, true)
    }
}