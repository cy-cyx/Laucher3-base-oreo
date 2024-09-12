package com.android.launcher3.custom

import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.util.AttributeSet
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import com.android.launcher3.Launcher

class CustomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs), Launcher.CustomContentCallbacks {

    init {
        setBackgroundColor(Color.BLUE)
        addView(ScrollView(context).apply {
            addView(LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                addView(Button(context).apply {
                    setOnClickListener {
                        Toast.makeText(context, "点击1", Toast.LENGTH_SHORT).show()
                    }
                }, LayoutParams(LayoutParams.WRAP_CONTENT, 500))
                addView(Button(context).apply {
                    setOnClickListener {
                        Toast.makeText(context, "点击2", Toast.LENGTH_SHORT).show()
                    }
                }, LayoutParams(LayoutParams.WRAP_CONTENT,500))
                addView(Button(context).apply {
                    setOnClickListener {
                        Toast.makeText(context, "点击3", Toast.LENGTH_SHORT).show()
                    }
                }, LayoutParams(LayoutParams.WRAP_CONTENT,500))
                addView(Button(context).apply {
                    setOnClickListener {
                        Toast.makeText(context, "点击4", Toast.LENGTH_SHORT).show()
                    }
                }, LayoutParams(LayoutParams.WRAP_CONTENT, 500))
                addView(Button(context).apply {
                    setOnClickListener {
                        Toast.makeText(context, "点击4", Toast.LENGTH_SHORT).show()
                    }
                }, LayoutParams(LayoutParams.WRAP_CONTENT, 500))
            })
        })
    }

    override fun onShow(fromResume: Boolean) {

    }

    override fun onHide() {

    }

    override fun onScrollProgressChanged(progress: Float) {
        Log.d("xx",progress.toString() + "");
    }

    override fun isScrollingAllowed(): Boolean {
        return true;
    }

}