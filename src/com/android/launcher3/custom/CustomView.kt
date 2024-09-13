package com.android.launcher3.custom

import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import com.android.launcher3.Launcher
import com.android.launcher3.databinding.LayoutCustomViewBinding

class CustomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs), Launcher.CustomContentCallbacks {

    init {
        LayoutCustomViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun onShow(fromResume: Boolean) {

    }

    override fun onHide() {

    }

    override fun onScrollProgressChanged(progress: Float) {

    }

    override fun isScrollingAllowed(): Boolean {
        return true;
    }

}