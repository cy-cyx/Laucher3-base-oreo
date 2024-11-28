package com.android.launcher3.custom

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.android.launcher3.Launcher
import com.android.launcher3.databinding.LayoutCustomViewBinding
import com.lambda.common.statistics.EventName
import com.lambda.common.statistics.EventUtil

class CustomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs), Launcher.CustomContentCallbacks {


    init {
        LayoutCustomViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun onShow(fromResume: Boolean) {
        EventUtil.logEvent(EventName.LNews, Bundle())
    }

    override fun onHide() {

    }

    override fun onScrollProgressChanged(progress: Float) {

    }

    override fun isScrollingAllowed(): Boolean {
        return true;
    }

}