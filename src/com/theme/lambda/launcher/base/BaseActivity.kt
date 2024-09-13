package com.theme.lambda.launcher.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    lateinit var viewBinding: T

    abstract fun initViewBinding(layoutInflater: LayoutInflater): T


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = initViewBinding(LayoutInflater.from(this))
        setContentView(viewBinding.root)
    }
}