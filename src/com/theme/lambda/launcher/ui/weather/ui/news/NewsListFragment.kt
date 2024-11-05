package com.lambdaweather.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.launcher3.databinding.FragmentNewsListBinding
import com.lambdaweather.adapter.NewsListAdapter
import com.lambdaweather.data.model.MyLocationModel
import com.lambdaweather.factory.NewsViewModelFactory
import com.lambdaweather.factory.RetrofitFactory
import com.lambdaweather.utils.GsonUtil
import com.lambdaweather.utils.LocalUtils
import com.lambdaweather.utils.WeatherUtils
import com.theme.lambda.launcher.base.BaseFragment

class NewsListFragment : BaseFragment<FragmentNewsListBinding>()  {
    private lateinit var viewModel: NewsViewModel
    private val mAdapter: NewsListAdapter by lazy { NewsListAdapter(this) }
    private var mLocation: MyLocationModel? = null
    private var mCountry: String = ""
    lateinit var factory: NewsViewModelFactory
    override fun initViewBinding(inflater: LayoutInflater): FragmentNewsListBinding {
        return FragmentNewsListBinding.inflate(inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        factory = NewsViewModelFactory(RetrofitFactory.appRepositorySource)
        viewModel = ViewModelProvider(this, factory).get(NewsViewModel::class.java)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun initView() {
        viewBinding.tvLeft.setOnClickListener {
           requireActivity().finish()
        }
        mLocation =  WeatherUtils.getSelectLocation()
        mCountry = mLocation?.country ?: LocalUtils.getCountry()
        viewModel.initNewsNext(
            "",
            "",
            mCountry
        )
        viewModel.newList.observe(viewLifecycleOwner) {
            if (it.data?.pageL == 0) {
                it.data.d?.news?.let { it1 -> mAdapter.setList(it1) }
            } else {
                it.data?.d?.news?.let { it1 -> mAdapter.addData(it1) }
            }
            mAdapter.loadMoreModule.loadMoreComplete()
        }
        initRefresh()
        initAdapter()
    }

    private fun initAdapter() {
        val manager = LinearLayoutManager(context)
        viewBinding.rv.layoutManager = manager
        viewBinding.rv.adapter = mAdapter
        mAdapter.setOnItemClickListener { _, _, position ->
//            navigation(
//                NewsListFragmentDirections.actionNewsListFragmentToNewsDetailsFragment(
//                    FragmentParam(GsonUtil.toJson(mAdapter.getItem(position)))
//                )
//            )
            (requireActivity() as NewsListActivity).goto(GsonUtil.toJson(mAdapter.getItem(position)))
        }
        mAdapter.loadMoreModule.setOnLoadMoreListener {
            viewModel.getNewsNext("", "", mCountry)
        }
    }

    private fun initRefresh() {
        val listener: SwipeRefreshLayout.OnRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            viewModel.initNewsNext("", "", mCountry)
            viewBinding.srl.isRefreshing = false
        }
        viewBinding.srl.setOnRefreshListener(listener)
    }


}