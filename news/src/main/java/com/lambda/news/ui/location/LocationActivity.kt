package com.lambda.news.ui.location

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.lambda.common.base.BaseActivity
import com.lambda.common.utils.CommonUtil
import com.lambda.common.utils.StatusBarUtil
import com.lambda.common.utils.marginStatusBarHeight
import com.lambda.news.data.CategoriesManager
import com.lambda.news.data.LocalManager
import com.lambda.news.databinding.NewsActivityLocationBinding
import com.lambda.news.widget.CountrySelectPopupWindow
import com.lambda.news.widget.LanguageSelectPopupWindow

class LocationActivity : BaseActivity<NewsActivityLocationBinding>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, LocationActivity::class.java))
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): NewsActivityLocationBinding {
        return NewsActivityLocationBinding.inflate(layoutInflater)
    }

    private var isHasChange = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)
        viewBinding.containerFl.marginStatusBarHeight()

        viewBinding.backIv.setOnClickListener {
            if (isHasChange) {
                CategoriesManager.upDataNewsData()
            }
            finish()
        }

        LocalManager.getNewsCountryBean().run {
            viewBinding.countryTv.setText(this.country)
            viewBinding.countryIv.setImageResource(this.icon)
        }

        LocalManager.getNewSLanguageBean().run {
            viewBinding.languageTv.setText(this.language)
        }

        viewBinding.locationFl.setOnClickListener {
            CountrySelectPopupWindow(this).apply {
                clickCountryCallback = {
                    viewBinding.countryIv.setImageResource(it.icon)
                    viewBinding.countryTv.setText(it.country)

                    LocalManager.setNewsCountry(it.countryCode)
                    isHasChange = true
                }
            }.showAsDropDown(
                viewBinding.locationFl,
                0,
                CommonUtil.dp2px(5f)
            )
        }

        viewBinding.languageFl.setOnClickListener {
            LanguageSelectPopupWindow(this).apply {
                clickLanguageCallback = {
                    viewBinding.languageTv.setText(it.language)

                    LocalManager.setNewsLanguage(it.languageCode)

                    isHasChange = true
                }
            }.showAsDropDown(
                viewBinding.languageFl,
                0,
                CommonUtil.dp2px(5f)
            )
        }
    }

    override fun onBackPressed() {
        if (isHasChange) {
            CategoriesManager.upDataNewsData()
        }
        super.onBackPressed()
    }
}