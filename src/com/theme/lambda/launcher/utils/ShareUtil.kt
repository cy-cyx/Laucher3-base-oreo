package com.theme.lambda.launcher.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object ShareUtil {

    fun openWebView(context: Context, url: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        intent.data = Uri.parse(url)
        context.startActivity(Intent.createChooser(intent, "web"))
    }
}