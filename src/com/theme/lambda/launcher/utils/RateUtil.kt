package com.theme.lambda.launcher.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import com.android.launcher3.R

object RateUtil {

    fun rate(context: Context) {
        val playPackage = "com.android.vending"
        try {
            val currentPackageName = context.packageName
            if (currentPackageName != null) {
                val currentPackageUri = Uri.parse("market://details?id=" + context.packageName)
                val intent = Intent(Intent.ACTION_VIEW, currentPackageUri)
                intent.setPackage(playPackage)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val currentPackageUri =
                Uri.parse("https://play.google.com/store/apps/details?id=" + context.packageName)
            val intent = Intent(Intent.ACTION_VIEW, currentPackageUri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    fun contactUs(context: Context) {
        try {
            context.startActivity(
                Intent.createChooser(
                    Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:")
                        putExtra(Intent.EXTRA_EMAIL, arrayOf("henryalexanderccuii@gmail.com"))
                        putExtra(
                            Intent.EXTRA_SUBJECT,
                            "${context.getString(R.string.app_name)} feedback"
                        )
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "Your device brand: ${Build.BRAND}\nYour system version: ${Build.VERSION.RELEASE}\n" +
                                    "Your app version:${SystemUtil.getSystemVersion()}\nYour feedback or suggestion:"
                        )
                    }, "feedback"
                )
            )
        } catch (e: Exception) {

        }
    }
}