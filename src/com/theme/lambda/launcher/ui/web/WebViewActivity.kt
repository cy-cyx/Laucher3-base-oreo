package com.theme.lambda.launcher.ui.web

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.android.launcher3.databinding.ActivityWebviewBinding
import com.lambda.common.base.BaseActivity
import com.lambda.common.utils.gone
import com.lambda.common.utils.visible
import com.lambda.common.utils.StatusBarUtil

class WebViewActivity : BaseActivity<ActivityWebviewBinding>() {

    companion object {

        val keyUrl = "key_url"

        fun start(context: Context, url: String) {
            context.startActivity(Intent(context, WebViewActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra(keyUrl, url)
            })
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityWebviewBinding {
        return ActivityWebviewBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)

        val url = intent.getStringExtra(keyUrl) ?: ""

        viewBinding.webViewWv.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, url);
            }
        })
        viewBinding.webViewWv.setWebChromeClient(object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                viewBinding.titleTv.text = title
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    viewBinding.progressBar.gone()
                } else {
                    viewBinding.progressBar.visible()
                    viewBinding.progressBar.setProgress(newProgress, true)
                }
            }
        });

        viewBinding.webViewWv.loadUrl(url)

        viewBinding.backIv.setOnClickListener {
            finish()
        }
    }

    override fun onBackPressed() {
        if (viewBinding.webViewWv.canGoBack()) {
            viewBinding.webViewWv.goBack()
        } else {
            super.onBackPressed()
        }
    }
}