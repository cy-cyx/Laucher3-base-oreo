package com.theme.lambda.launcher.widget.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.launcher3.R
import com.android.launcher3.databinding.DialogSelectAppBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.theme.lambda.launcher.appinfo.AppInfo
import com.theme.lambda.launcher.widget.adapter.SelectAppDialogAdapter

class SelectAppDialog(context: Context) :
    BottomSheetDialog(context, R.style.Theme_translucentDialog) {

    var selectAppListen: ((AppInfo) -> Unit)? = null

    val viewBinding = DialogSelectAppBinding.inflate(LayoutInflater.from(context))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        val params = window?.attributes
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        window?.attributes = params

        viewBinding.appInfoRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SelectAppDialogAdapter().apply {
                itemClickListen = selectAppListen
            }
        }
    }
}

