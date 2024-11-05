package com.lambdaweather.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.launcher3.R
import com.android.launcher3.databinding.FragmentNewsDetailsBinding
import com.lambdaweather.LambdaWeather
import com.lambdaweather.adapter.NewsListBottomAdapter
import com.lambdaweather.data.model.NewsModel
import com.lambdaweather.factory.NewsViewModelFactory
import com.lambdaweather.factory.RetrofitFactory
import com.lambdaweather.utils.GlideUtil
import com.lambdaweather.utils.GsonUtil
import com.lambdaweather.utils.IntentUtils
import com.lambdaweather.utils.LocalUtils
import com.lambdaweather.utils.ScreenUtils
import com.theme.lambda.launcher.base.BaseFragment
import java.util.regex.Pattern


class NewsDetailsFragment: BaseFragment<FragmentNewsDetailsBinding>() {

    private lateinit var viewModel: NewsViewModel
    private val mAdapter: NewsListBottomAdapter by lazy { NewsListBottomAdapter(this) }
    lateinit var factory: NewsViewModelFactory
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
        val news = GsonUtil.fromJson(arguments?.getString("params"), NewsModel.NewsDTO::class.java)
        if (news != null) {
            update(news)
        }

    }

    private fun update(news: NewsModel.NewsDTO) {
        news.image_urls?.get(0)?.let { it1 ->
            GlideUtil.loadUrlImage(
                this,
                it1, viewBinding.ivPic, ph = null
            )
        }
        if (news != null) {
            initBottom(news)
        }
        viewBinding.ivRight.setOnClickListener {
            news.link?.let { it1 -> IntentUtils.intentUri(requireContext(), it1) }
        }

        viewBinding.tvBarName.text = news.link
        viewBinding.tvUrl.text = news.link
        viewBinding.tvTime.text = news.publishDate.toString()
        viewBinding.tvAuthor.text = news.author
        viewBinding.tvTitle.text = HtmlCompat.fromHtml(
            news.title!!,
            HtmlCompat.FROM_HTML_MODE_COMPACT
        ).toString()
        viewBinding.tvDes.text = news.description
        news.content?.let {
            initContent(
                HtmlCompat.fromHtml(
                    it,
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                ).toString()
            )
        }
    }

    private fun initContent(text: String) {
        val flLayout = FrameLayout(requireContext())
        val pattern = "[.,]"
        val splitter = Pattern.compile(pattern)
        val texts = splitter.split(text)
        val temps = mutableListOf<String>()
        texts.forEach {
            temps.add(it)
        }
        text.let {
            val textView = TextView(requireContext())
            textView.text = temps.toString()
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            textView.textSize = 18f
            textView.setLineSpacing(0f, 1.6f)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            //设置权重
            //params.weight=1.0f;
            //设置间距
            textView.setPadding(0, ScreenUtils.dp2px(LambdaWeather.application, 20), 0, 0)
            //设置布局参数
            textView.layoutParams = params
            viewBinding.llyContent.addView(textView, params)
        }
    }

    private fun initBottom(news: NewsModel.NewsDTO) {
        viewModel.newList.observe(viewLifecycleOwner) {
            if (it.data?.pageL == 0) {
                it.data.d?.news?.let { it1 -> mAdapter.setList(it1) }
            } else {
                it.data?.d?.news?.let { it1 -> mAdapter.addData(it1) }
            }
            mAdapter.loadMoreModule.loadMoreComplete()
        }
        initAdapter(news)
        viewModel.initNewsNext("", "", LocalUtils.getCountry())
    }

    private fun initAdapter(news: NewsModel.NewsDTO) {
        val manager = LinearLayoutManager(context)
        viewBinding.rv.layoutManager = manager
        viewBinding.rv.adapter = mAdapter
        mAdapter.setOnItemClickListener { _, _, position ->
            update(mAdapter.getItem(position))
        }
    }

    override fun initViewBinding(inflater: LayoutInflater): FragmentNewsDetailsBinding {
        return FragmentNewsDetailsBinding.inflate(inflater)
    }

}