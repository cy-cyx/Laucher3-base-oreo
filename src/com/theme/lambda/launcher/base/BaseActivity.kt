package com.theme.lambda.launcher.base

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.theme.lambda.launcher.utils.CommonUtil

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    lateinit var viewBinding: T

    abstract fun initViewBinding(layoutInflater: LayoutInflater): T


    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && CommonUtil.isTranslucentOrFloating(
                this
            )
        ) {
            val result: Boolean = CommonUtil.fixOrientation(this)
        }
        super.onCreate(savedInstanceState)
        viewBinding = initViewBinding(LayoutInflater.from(this))
        setContentView(viewBinding.root)
    }
}