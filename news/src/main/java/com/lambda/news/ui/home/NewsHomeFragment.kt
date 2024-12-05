package com.lambda.news.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.lambda.common.base.BaseFragment
import com.lambda.common.statistics.EventName
import com.lambda.common.statistics.EventUtil
import com.lambda.common.utils.CommonUtil
import com.lambda.common.widget.adapter.LauncherFragmentAdapter
import com.lambda.news.LambdaNews
import com.lambda.news.data.CategoriesManager
import com.lambda.news.data.LocalManager
import com.lambda.news.databinding.NewsFragmentHomeBinding
import com.lambda.news.ui.location.LocationActivity
import com.lambda.news.ui.newslist.NewsListFragment
import com.lambda.news.ui.sort.SortActivity

class NewsHomeFragment : BaseFragment<NewsFragmentHomeBinding>() {
    override fun initViewBinding(inflater: LayoutInflater): NewsFragmentHomeBinding {
        return NewsFragmentHomeBinding.inflate(inflater)
    }

    private val viewModel = NewsHomeViewModel()
    var mfrom = NewsHomeActivity.sFromCustom

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (mfrom == NewsHomeActivity.sFromCustom) {
            viewBinding.topFl.layoutParams.height =
                CommonUtil.dp2px(50f) + CommonUtil.getStatusBarHeight()
        }

        viewBinding.sortIv.setOnClickListener {
            if (LambdaNews.isInPreviewMode()) return@setOnClickListener
            SortActivity.start(requireContext())
            EventUtil.logEvent(EventName.LNewsList, Bundle().apply {
                putString("type", "add_tag")
            })
        }
        viewBinding.locationLl.setOnClickListener {
            LocationActivity.start(requireContext())
        }

        CategoriesManager.myCategoriesLiveData.observe(viewLifecycleOwner, Observer {
            bindData(it)
        })
    }

    private fun bindData(categories: ArrayList<String>) {
        viewBinding.countryTv.setText(LocalManager.getNewsCountryName())

        var temp = ArrayList(categories)
        temp.add(0, "Local")
        temp.add(1, "Headlines")

        // 移除旧
        childFragmentManager.fragments.forEach {
            childFragmentManager.beginTransaction().remove(it).commitNow()
        }

        viewBinding.tabTl.apply {
            temp.forEach {
                addTab(viewBinding.tabTl.newTab())
            }
        }
        viewBinding.tabTl.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (!EventUtil.hasLogNewsChangeTag) {
                    EventUtil.logEvent(EventName.LNewsList, Bundle().apply {
                        putString("type", "change_tag")
                    })
                    EventUtil.hasLogNewsChangeTag = true
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        viewBinding.themeVp.apply {

            adapter = LauncherFragmentAdapter(childFragmentManager).apply {

                // 多种主题
                temp.forEach {
                    fragments.add(NewsListFragment().apply {
                        category = it
                        from = mfrom
                    })

                    fragmentsTitle.add(it)
                }
            }

            // 负一屏支持快速滑动退出
            if (mfrom == NewsHomeActivity.sFromCustom) {
                viewBinding.themeVp.useFastWQuit = true
            }
        }
        viewBinding.tabTl.setupWithViewPager(viewBinding.themeVp)
    }
}