package com.theme.lambda.launcher.ui.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.launcher3.databinding.FragmentWeather2Binding
import com.lambda.common.utils.utilcode.util.PermissionUtils
import com.lambdaweather.AppViewModel
import com.lambdaweather.R
import com.lambdaweather.base.BaseFragment
import com.lambdaweather.data.model.ForestWeatherModel
import com.lambdaweather.data.model.HomeUiModel
import com.lambdaweather.data.model.NewsModel
import com.lambdaweather.data.model.WeatherModel
import com.lambdaweather.databinding.ItemAllLayoutHomeTopBinding
import com.lambdaweather.ui.news.NewsDetailsActivity
import com.lambdaweather.ui.news.NewsListActivity
import com.lambdaweather.utils.GlideUtil
import com.lambdaweather.utils.GsonUtil
import com.lambdaweather.utils.LocalUtils
import com.lambdaweather.utils.LocationManagerHelper
import com.lambdaweather.utils.ScaleUpAnimator
import com.lambdaweather.utils.TimeUtils
import com.lambdaweather.utils.WeatherUtils
import com.theme.lambda.launcher.utils.TimerUtils
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class WeatherFragment : BaseFragment<FragmentWeather2Binding>() {
    private val viewModel: AppViewModel by viewModel()
    private val mAdapter: HomeUiAdapter by lazy { HomeUiAdapter(this) }
    private val mHeaderView: View by lazy {
        layoutInflater.inflate(
            R.layout.item_all_layout_home_top, null, false
        )
    }
    private val headerBinding: ItemAllLayoutHomeTopBinding by lazy {
        ItemAllLayoutHomeTopBinding.bind(mHeaderView)
    }
    private val contracts =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val location = it.data?.getParcelableExtra<Location>("location")
            if (it.resultCode == RESULT_OK && location != null) {
                viewModel.locationBuild(location)
            } else {
                initHome()
            }
        }

    override fun initViewBinding(inflater: LayoutInflater): FragmentWeather2Binding {
        return FragmentWeather2Binding.inflate(inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.weatherView.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.mLocationSuccess.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.updateLocation()
                initAll()
            }
        }
        viewModel.mLocation.observe(viewLifecycleOwner) {
            if (null != it) {
                binding.tvTitle.text = it.getLocalName()
                viewModel.getWeather()
                viewModel.getForecastDay7Hour()
                viewModel.getForecastAir()
                viewModel.getNews(it.lat, it.lon, it.country ?: LocalUtils.getCountry())
            }
        }

        binding.rvHome.post {
            val locationModel = WeatherUtils.getSelectLocation()
            if (locationModel == null) {
                requestLocation()
            } else {
                viewModel.locationBuild(Location(locationModel.getLocalName()).apply {
                    latitude = locationModel.lat.toDouble()
                    longitude = locationModel.lon.toDouble()
                })
            }
        }
        binding.ivSettings.setOnClickListener {
            contracts.launch(Intent(requireActivity(), WeatherSettingsActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        binding.weatherView.onResume()
    }


    private fun initAll() {
        initRefresh()
        initAdapter()
        initHomeUiData()
        viewModel.weatherType.observe(this.viewLifecycleOwner) {
            binding.weatherView.setDrawerType(it)
        }
        initHome()
    }

    private fun initHome() {
        TimerUtils.getIpLocation()
        viewModel.weather.observe(this.viewLifecycleOwner) {
            binding.srl.isRefreshing = false
            if (it.data?.dt == null) {
                return@observe
            }
            it.data?.let { data ->
                updateWeather(data)
                initAdv(data)
                initTravel(data)
                sunriseSunset(data)
            }
        }

        viewModel.forecastHourWeather.observe(this.viewLifecycleOwner) {
            if (it.data?.cod == null) {
                return@observe
            }
            it.data?.let { data ->
                updateForecast(data)
            }
        }

        airQuality()
        forestDay7Weather()
        initLocalNew()
    }

    @SuppressLint("SetTextI18n")
    private fun updateWeather(model: WeatherModel) {
        headerBinding.includeLayoutHomeTop.tvTemperature.text =
            WeatherUtils.getTemp(model.main?.temp).roundToInt().toString()
        headerBinding.includeLayoutHomeTop.tvRealFeel.text = "${getString(R.string.realfeel)} ${
            WeatherUtils.getTemp(model.main?.temp).roundToInt()
                .toString() + WeatherUtils.getTempUnit()
        }"
        headerBinding.includeLayoutHomeTop.tvWeather.text =
            model.weather?.get(0)?.description.toString()
        headerBinding.includeLayoutHomeTop.tvWind.text = model.wind?.speed.toString() + "mph"
        GlideUtil.loadResImage(
            requireContext(),
            WeatherUtils.getIconUrl(model.weather?.get(0)?.icon),
            headerBinding.includeLayoutHomeTop.ivWeather
        )
        updateTempUnit()
    }

    @SuppressLint("SetTextI18n")
    private fun updateForecast(model: ForestWeatherModel) {
        headerBinding.includeLayoutHomeTop.tvMax.text = WeatherUtils.getTemp(
            model.getDailyMaxTemp(
                TimeUtils.getDateToString(
                    TimeUtils.getCurrentTimeMillis(), TimeUtils.TIME_TYPE
                )
            )
        ).roundToInt().toString() + WeatherUtils.getTempUnit()
        headerBinding.includeLayoutHomeTop.tvMin.text = WeatherUtils.getTemp(
            model.getDailyMinTemp(
                TimeUtils.getDateToString(
                    TimeUtils.getCurrentTimeMillis(), TimeUtils.TIME_TYPE
                )
            )
        ).roundToInt().toString() + WeatherUtils.getTempUnit()
        swapOrSetItem(
            mAdapter.data[HomeUiModel.HomeWidget.HOURLY.type].copy(forestWeatherModel = model)
        )
    }

    private fun initTravel(model: WeatherModel) {
        swapOrSetItem(
            mAdapter.data[HomeUiModel.HomeWidget.TRAVEL.type].copy(weatherModel = model)
        )
    }

    private fun initAdv(model: WeatherModel) {
        swapOrSetItem(
            mAdapter.data[HomeUiModel.HomeWidget.INDEX.type].copy(weatherModel = model)
        )
    }

    private fun airQuality() {
        viewModel.forecastAir.observe(this.viewLifecycleOwner) {
            swapOrSetItem(
                mAdapter.data[HomeUiModel.HomeWidget.AIR.type].copy(airModel = it.data)
            )
        }
    }

    private fun forestDay7Weather() {
        viewModel.forecastDay7Weather.observe(this.viewLifecycleOwner) {
            swapOrSetItem(
                mAdapter.data[HomeUiModel.HomeWidget.DAILY.type].copy(forestWeatherDay7Model = it.data)
            )
        }
    }

    private fun sunriseSunset(model: WeatherModel) {
        swapOrSetItem(mAdapter.data[HomeUiModel.HomeWidget.SUN.type].copy(weatherModel = model))
    }

    fun intentToNews() {
        startActivity(Intent(requireContext(), NewsListActivity::class.java))
    }

    fun intentToNew(news: NewsModel.NewsDTO) {
        startActivity(Intent(requireContext(), NewsDetailsActivity::class.java).apply {
            putExtra("params", GsonUtil.toJson(news))
        })
    }

    private fun initLocalNew() {
        viewModel.newList.observe(this.viewLifecycleOwner) { model ->
            if (((model.data?.d?.news?.size ?: 0) == 0)) {
                return@observe
            }

            swapOrSetItem(
                mAdapter.data[HomeUiModel.HomeWidget.NEWS.type].copy(newsModel = model.data)
            )
        }
    }

    private val listener: SwipeRefreshLayout.OnRefreshListener by lazy {
        SwipeRefreshLayout.OnRefreshListener {
            initHomeUiData()
            viewModel.updateRefreshLocation()
        }
    }

    private fun initRefresh() {
        binding.srl.setOnRefreshListener(listener)
    }

    private fun initAdapter() {
        val manager = LinearLayoutManager(context)
        binding.rvHome.layoutManager = manager
        mAdapter.setOnItemClickListener { _, _, _ ->

        }
        val anim = ScaleUpAnimator()
        anim.supportsChangeAnimations = true
        anim.changeDuration = 500
        binding.rvHome.itemAnimator = anim
        binding.rvHome.adapter = ScaleInAnimationAdapter(mAdapter)
        binding.rvHome.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun swapOrSetItem(homeUi: HomeUiModel) {
        var indexT = -1
        mAdapter.data.forEachIndexed { index, homeUiModel ->
            if (homeUiModel.itemType == homeUi.itemType) {
                indexT = index
                return@forEachIndexed
            }
        }
        if (indexT != -1) {
            mAdapter.setData(indexT, homeUi)
        } else {
            mAdapter.addData(homeUi)
        }
    }

    private fun initHomeUiData() {
        if (mAdapter.data.size == 0) {
            mAdapter.setHeaderView(mHeaderView)
            headerBinding.includeLayoutHomeTop.clTop.setOnClickListener {

            }
            val new = HomeUiModel(HomeUiModel.HomeWidget.NEWS.type, null, null)
            val list = arrayListOf(
                new,
                new.copy(itemType = HomeUiModel.HomeWidget.HOURLY.type),
                new.copy(itemType = HomeUiModel.HomeWidget.DAILY.type),
                new.copy(itemType = HomeUiModel.HomeWidget.AD.type),
                new.copy(itemType = HomeUiModel.HomeWidget.TRAVEL.type),
                new.copy(itemType = HomeUiModel.HomeWidget.INDEX.type),
                new.copy(itemType = HomeUiModel.HomeWidget.AIR.type),
                new.copy(itemType = HomeUiModel.HomeWidget.SUN.type),
            )
            mAdapter.setList(list)
        } else {
            mAdapter.data.forEachIndexed { index, homeUiModel ->
                mAdapter.setData(index, homeUiModel.apply {
                    this.airModel = null
                    this.newsModel = null
                    this.forestWeatherModel = null
                    this.weatherModel = null
                    this.forestWeatherDay7Model = null
                })
            }
        }
    }


    private fun updateTempUnit() {
        headerBinding.includeLayoutHomeTop.tvUnit.text = WeatherUtils.getTempUnit()
    }

    private fun requestLocation() {
        PermissionUtils.permission(Manifest.permission.ACCESS_COARSE_LOCATION)
            .callback(object : PermissionUtils.SimpleCallback {
                override fun onGranted() {
                    if (!isAdded) return
                    Toast.makeText(requireContext(), R.string.location_hint, Toast.LENGTH_LONG)
                        .show()
                    LocationManagerHelper.instance.build(requireActivity()) { location, _ ->
                        if (location == null) {
                            ipLocation()
                        } else {
                            viewModel.locationBuild(location)
                        }
                    }
                }

                override fun onDenied() {
                    if (!isAdded) return
                    Toast.makeText(requireContext(), R.string.loading, Toast.LENGTH_SHORT).show()
                    ipLocation()
                }
            }).request()
    }

    private fun ipLocation() {
        viewModel.ipModel.observe(this) {
            if (it.appException != null) {
                return@observe
            }
            viewModel.locationBuild(Location(it.data?.city).apply {
                latitude = it?.data?.latitude ?: 0.0
                longitude = it?.data?.longitude ?: 0.0
            })
        }
        viewModel.getIp()
    }
}