package com.lambda.news.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import com.lambda.common.Constants
import com.lambda.common.base.BaseFragment
import com.lambda.common.widget.adapter.LauncherFragmentAdapter
import com.lambda.news.data.CategoriesManager
import com.lambda.news.databinding.NewsFragmentHomeBinding
import com.lambda.news.ui.newslist.NewsListFragment
import com.lambda.news.ui.sort.SortActivity
import net.pubnative.lite.sdk.vpaid.models.vast.BlockedAdCategories

class HomeFragment : BaseFragment<NewsFragmentHomeBinding>() {
    override fun initViewBinding(inflater: LayoutInflater): NewsFragmentHomeBinding {
        return NewsFragmentHomeBinding.inflate(inflater)
    }

    private val viewModel = HomeViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.sortIv.setOnClickListener {
            SortActivity.start(requireContext())
        }

        CategoriesManager.myCategoriesLiveData.observe(viewLifecycleOwner, Observer {
            bindData(it)
        })
    }

    private fun bindData(categories: ArrayList<String>){
        categories.add(0,"Local")

        // 移除旧
        childFragmentManager.fragments.forEach {
            childFragmentManager.beginTransaction().remove(it).commitNow()
        }

        viewBinding.tabTl.apply {
            categories.forEach {
                addTab(viewBinding.tabTl.newTab())
            }
        }

        viewBinding.themeVp.apply {

            adapter = LauncherFragmentAdapter(childFragmentManager).apply {

                // 多种主题
                categories.forEach {
                    fragments.add(NewsListFragment().apply {
                        category = it
                    })

                    fragmentsTitle.add(it)
                }
            }
        }
        viewBinding.tabTl.setupWithViewPager(viewBinding.themeVp)
    }
}