package com.theme.lambda.launcher.ui.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.android.launcher3.databinding.FragmentCustomBinding
import com.lambda.common.base.BaseFragment
import com.theme.lambda.launcher.widget.adapter.LauncherFragmentAdapter
import com.theme.lambda.launcher.ui.news.NewsFragment

class CustomFragment : BaseFragment<FragmentCustomBinding>() {

    override fun initViewBinding(inflater: LayoutInflater): FragmentCustomBinding {
        return FragmentCustomBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (viewBinding.tabTl.layoutParams as FrameLayout.LayoutParams).topMargin =
            com.lambda.common.utils.CommonUtil.getStatusBarHeight() + com.lambda.common.utils.CommonUtil.dp2px(20f)

        viewBinding.tabTl.apply {
            addTab(viewBinding.tabTl.newTab())
        }

        viewBinding.newVp.adapter = LauncherFragmentAdapter(childFragmentManager).apply {
            fragments.add(NewsFragment())

            fragmentsTitle.add("Latest")
        }
        viewBinding.tabTl.setupWithViewPager(viewBinding.newVp)
    }
}