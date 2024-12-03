package com.lambda.news.ui.sort

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.lambda.common.base.BaseActivity
import com.lambda.common.utils.StatusBarUtil
import com.lambda.common.utils.marginStatusBarHeight
import com.lambda.news.data.CategoriesManager
import com.lambda.news.databinding.NewsActivitySortBinding
import com.lambda.news.widget.SortItemView

class SortActivity : BaseActivity<NewsActivitySortBinding>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SortActivity::class.java))
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): NewsActivitySortBinding {
        return NewsActivitySortBinding.inflate(layoutInflater)
    }

    private var myCategories = ArrayList<String>()
    private var allCategories = ArrayList<String>()

    private var isChange = false

    private val sortStatusChangeCallback: ((Boolean, String) -> Unit) = { isAdd, category ->
        isChange = true
        if (isAdd) {
            if (!myCategories.contains(category)) {
                myCategories.add(category)
            }
        } else {
            myCategories.remove(category)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)
        viewBinding.containerLl.marginStatusBarHeight()

        myCategories = ArrayList(CategoriesManager.myCategoriesLiveData.value ?: arrayListOf())
        allCategories = ArrayList(CategoriesManager.allCategories)

        var showSelectFlexView = false
        var showRemFlexView = false

        allCategories.forEach {
            if (myCategories.contains(it)) {
                showSelectFlexView = true
                viewBinding.selectTopicsFl.addView(SortItemView(this).apply {
                    bindData(true, it)
                    stateChangeCallback = sortStatusChangeCallback
                })
            } else {
                showRemFlexView = true
                viewBinding.recommendedTopicFl.addView(SortItemView(this).apply {
                    bindData(false, it)
                    stateChangeCallback = sortStatusChangeCallback
                })
            }
        }
        viewBinding.selectTopicsFl.visibility = if (showSelectFlexView) View.VISIBLE else View.GONE
        viewBinding.recommendedTopicFl.visibility = if (showRemFlexView) View.VISIBLE else View.GONE

        viewBinding.backIv.setOnClickListener {
            if (isChange) CategoriesManager.upDataMyCategories(myCategories)
            finish()
        }
    }

    override fun onBackPressed() {
        if (isChange) CategoriesManager.upDataMyCategories(myCategories)
        super.onBackPressed()
    }
}