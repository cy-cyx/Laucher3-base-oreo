package com.lambda.news.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import com.lambda.common.base.BaseFragment
import com.lambda.common.statistics.EventName
import com.lambda.common.statistics.EventUtil
import com.lambda.common.widget.adapter.LauncherFragmentAdapter
import com.lambda.news.data.CategoriesManager
import com.lambda.news.databinding.NewsFragmentHomeBinding
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

        viewBinding.sortIv.setOnClickListener {
            SortActivity.start(requireContext())
            EventUtil.logEvent(EventName.LNewsList, Bundle().apply {
                putString("from", "add_tag")
            })
        }

        CategoriesManager.myCategoriesLiveData.observe(viewLifecycleOwner, Observer {
            bindData(it)
        })
    }

    private fun bindData(categories: ArrayList<String>){
        var temp = ArrayList(categories)
        temp.add(0,"Local")

        // 移除旧
        childFragmentManager.fragments.forEach {
            childFragmentManager.beginTransaction().remove(it).commitNow()
        }

        viewBinding.tabTl.apply {
            temp.forEach {
                addTab(viewBinding.tabTl.newTab())
            }
        }

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
        }
        viewBinding.tabTl.setupWithViewPager(viewBinding.themeVp)
    }
}