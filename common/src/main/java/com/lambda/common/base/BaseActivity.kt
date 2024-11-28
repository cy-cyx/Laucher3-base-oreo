package com.lambda.common.base

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.lambda.common.utils.CommonUtil
import com.lambda.common.utils.PermissionUtil


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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    fun showKeyBoard(view: View) {
        view.requestFocus()
        (getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(
            view,
            0
        )
    }
}