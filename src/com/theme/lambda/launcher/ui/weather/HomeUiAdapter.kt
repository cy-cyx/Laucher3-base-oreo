package com.theme.lambda.launcher.ui.weather

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lambdaweather.LambdaWeather
import com.lambdaweather.R
import com.lambdaweather.adapter.AirQualityAdapter
import com.lambdaweather.adapter.DailyForecastAdapter
import com.lambdaweather.adapter.HomeAdvAdapter
import com.lambdaweather.adapter.HomeHourAdapter
import com.lambdaweather.adapter.HomeTravelAdapter
import com.lambdaweather.data.model.AdvModel
import com.lambdaweather.data.model.AirModel
import com.lambdaweather.data.model.ForestDayWeatherModel
import com.lambdaweather.data.model.ForestWeatherModel
import com.lambdaweather.data.model.HomeUiModel
import com.lambdaweather.data.model.NewsModel
import com.lambdaweather.data.model.WeatherModel
import com.lambdaweather.databinding.ItemAllLayoutHomeAdBinding
import com.lambdaweather.databinding.ItemAllLayoutHomeAirBinding
import com.lambdaweather.databinding.ItemAllLayoutHomeClothBinding
import com.lambdaweather.databinding.ItemAllLayoutHomeDailyBinding
import com.lambdaweather.databinding.ItemAllLayoutHomeHourlyBinding
import com.lambdaweather.databinding.ItemAllLayoutHomeIndexBinding
import com.lambdaweather.databinding.ItemAllLayoutHomeNewBinding
import com.lambdaweather.databinding.ItemAllLayoutHomeSunsetBinding
import com.lambdaweather.databinding.ItemAllLayoutHomeTravelBinding
import com.lambdaweather.utils.AqiUtil
import com.lambdaweather.utils.GlideUtil
import com.lambdaweather.utils.ScreenUtils
import com.lambdaweather.utils.TimeUtils
import com.lambdaweather.utils.WeatherUtils
import com.lambdaweather.utils.gone
import com.lambdaweather.utils.visible
import com.lambdaweather.view.RecyclerViewBanner
import com.lambdaweather.view.dynamicweather.BaseDrawer
import kotlin.math.roundToInt

class HomeUiAdapter(val fragment: WeatherFragment) :
    BaseMultiItemQuickAdapter<HomeUiModel, BaseViewHolder>() {

    init {
        addItemType(HomeUiModel.HomeWidget.NEWS.type, R.layout.item_all_layout_home_new)
        addItemType(HomeUiModel.HomeWidget.HOURLY.type, R.layout.item_all_layout_home_hourly)
        addItemType(HomeUiModel.HomeWidget.DAILY.type, R.layout.item_all_layout_home_daily)
        addItemType(HomeUiModel.HomeWidget.TRAVEL.type, R.layout.item_all_layout_home_travel)
        addItemType(HomeUiModel.HomeWidget.INDEX.type, R.layout.item_all_layout_home_index)
        addItemType(HomeUiModel.HomeWidget.AIR.type, R.layout.item_all_layout_home_air)
        addItemType(HomeUiModel.HomeWidget.SUN.type, R.layout.item_all_layout_home_sunset)
        addItemType(HomeUiModel.HomeWidget.AD.type, R.layout.item_all_layout_home_ad)
    }

    override fun convert(holder: BaseViewHolder, item: HomeUiModel) {
        when (item.itemType) {
            HomeUiModel.HomeWidget.NEWS.type -> {
                initNews(holder, item.newsModel)
            }

            HomeUiModel.HomeWidget.HOURLY.type -> {
                initHourly(holder, item.forestWeatherModel)
            }

            HomeUiModel.HomeWidget.DAILY.type -> {
                initDaily(holder, item.forestWeatherDay7Model)
            }

            HomeUiModel.HomeWidget.TRAVEL.type -> {
                initTravel(holder, item.weatherModel)
            }

            HomeUiModel.HomeWidget.INDEX.type -> {
                initAdv(holder, item.weatherModel)
            }

            HomeUiModel.HomeWidget.AIR.type -> {
                initAirQuality(holder, item.airModel)
            }

            HomeUiModel.HomeWidget.SUN.type -> {
                initSunriseSunset(holder, item.weatherModel)
            }

            HomeUiModel.HomeWidget.AD.type -> {
                val binding = ItemAllLayoutHomeAdBinding.bind(holder.itemView)
                val layoutP = binding.root.layoutParams
                layoutP.height = ViewGroup.LayoutParams.WRAP_CONTENT
                binding.root.layoutParams = layoutP
                LambdaWeather.showNativeAd(holder.getView(R.id.fl_list_ad))
            }
        }
    }

    private fun initNews(holder: BaseViewHolder, model: NewsModel?) {
        val binding = ItemAllLayoutHomeNewBinding.bind(holder.itemView)
        val layoutP = binding.root.layoutParams
        if (model == null) {
            layoutP.height = 0
            binding.root.layoutParams = layoutP
            binding.root.gone()
            return
        }
        layoutP.height = ViewGroup.LayoutParams.WRAP_CONTENT
        binding.root.visible()
        if ((model.d?.news?.size ?: 0) > 4) {
            binding.includedLayoutHomeNew.rvBanner.setRvBannerData(
                model.d?.news?.subList(
                    0, 4
                )
            )
        } else {
            val temp = mutableListOf<NewsModel.NewsDTO>()
            model.d?.news?.let { temp.addAll(it) }
            when (model.d?.news?.size) {
                1 -> {
                    temp.addAll(model.d!!.news!!)
                    temp.addAll(model.d!!.news!!)
                    temp.addAll(model.d!!.news!!)
                }

                2 -> {
                    temp.addAll(model.d!!.news!!)
                }

                3 -> {
                    temp.add(model.d!!.news!![0])
                }
            }
            binding.includedLayoutHomeNew.rvBanner.setRvBannerData(temp)
        }
        binding.includedLayoutHomeNew.rvBanner.setOnSwitchRvBannerListener(object :
            RecyclerViewBanner.OnSwitchRvBannerListener {
            override fun switchBanner(
                position: Int, bannerView: AppCompatImageView?, fl: FrameLayout?
            ) {
                bannerView?.let {
                    model.d?.news?.get(position)?.image_urls?.get(0)?.let { it1 ->
                        GlideUtil.loadUrlImage(
                            fragment, it1, it, ph = R.drawable.ic_news_ph
                        )
                    }
                    binding.includedLayoutHomeNew.tvTitle.text = HtmlCompat.fromHtml(
                        model.d?.news?.get(position)?.title!!, HtmlCompat.FROM_HTML_MODE_COMPACT
                    ).toString()
                }
            }
        })
        binding.includedLayoutHomeNew.clAll.setOnClickListener {
            fragment.intentToNews()
        }

        binding.includedLayoutHomeNew.rvBanner.setOnRvBannerClickListener(object :
            RecyclerViewBanner.OnRvBannerClickListener {
            override fun onClick(position: Int) {
                fragment.intentToNew(
                    model.d?.news?.get(position)!!
                )
            }
        })

    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun initHourly(holder: BaseViewHolder, model: ForestWeatherModel?) {
        val binding = ItemAllLayoutHomeHourlyBinding.bind(holder.itemView)
        val layoutP = binding.root.layoutParams
        if (model == null) {
            //layoutP.height = 0
            //binding.root.layoutParams = layoutP
            binding.root.gone()
            return
        }
        layoutP.height = ViewGroup.LayoutParams.WRAP_CONTENT
        binding.root.visible()
        val hourTemp = mutableListOf<Int>()
        val hourTempList = mutableListOf<ForestWeatherModel.ListDTO>()
        var hourLowTemp: Int? = null
        model.list?.subList(0, 5)?.forEach {
            val temp = WeatherUtils.getTemp(it.main?.temp).roundToInt()
            hourTemp.add(temp)
            hourTempList.add(it)
            if (null == hourLowTemp) {
                hourLowTemp = temp
            } else if (hourLowTemp!! > temp) {
                hourLowTemp = temp
            }
        }

        binding.includedLayoutHomeHourly.whv.setUnit("°")
        binding.includedLayoutHomeHourly.whv.setTempDay(hourTemp.toIntArray())
        val hourLowX = mutableListOf<Int>()
        hourTemp.forEach { _ ->
            hourLowX.add(hourLowTemp!! - 1)
        }
        binding.includedLayoutHomeHourly.whv.setTempNight(hourLowX.toIntArray())
        binding.includedLayoutHomeHourly.whv.post {
            binding.includedLayoutHomeHourly.whv.invalidate()
        }
        val adapterHour: HomeHourAdapter = HomeHourAdapter(R.layout.item_home_hour)
        binding.includedLayoutHomeHourly.rv.layoutManager = GridLayoutManager(context, 5)
        binding.includedLayoutHomeHourly.rv.adapter = adapterHour
        adapterHour.setList(hourTempList)
    }

    @SuppressLint("UnsafeOptInUsageError", "SetTextI18n")
    private fun initDaily(
        holder: BaseViewHolder, model: ForestDayWeatherModel?
    ) {
        val binding = ItemAllLayoutHomeDailyBinding.bind(holder.itemView)
        val layoutP = binding.root.layoutParams
        if (model == null) {
            //layoutP.height = 0
            //binding.root.layoutParams = layoutP
            binding.root.gone()
            return
        }
        layoutP.height = ViewGroup.LayoutParams.WRAP_CONTENT
        binding.root.visible()

        val adapter = DailyForecastAdapter(R.layout.item_daily_weather)
        val manager = LinearLayoutManager(context)
        binding.includedLayoutHomeDaily.rv.layoutManager = manager
        binding.includedLayoutHomeDaily.rv.adapter = adapter
        adapter.setList(model.list)
        val highTemp = mutableListOf<Int>()
        val lowTemp = mutableListOf<Int>()
        var rainDay = 0
        model.list.forEachIndexed { _, listDTO ->
            TimeUtils.getDateToString(listDTO.dt * 1000L, TimeUtils.TIME_TYPE)
            highTemp.add(WeatherUtils.getTemp(listDTO.temp.max).roundToInt())
            lowTemp.add(WeatherUtils.getTemp(listDTO.temp.min).roundToInt())
            if (listDTO.weather[0].main.contains("rain", true)) {
                rainDay++
            }
        }
        var lowesTemp: Int = lowTemp[0]
        var lowesIndex = 0
        lowTemp.forEachIndexed { index, i ->
            if (lowesTemp > i) {
                lowesTemp = i
                lowesIndex = index
            }
        }
        binding.includedLayoutHomeDaily.includeViewHomeDailyBottom.tvLowTemp.text =
            lowesTemp.toString() + WeatherUtils.getTempUnit()
        binding.includedLayoutHomeDaily.includeViewHomeDailyBottom.tv1.text =
            "${context.getString(R.string.lowest_temperature)}(${
                TimeUtils.getDateToString(
                    model.list[lowesIndex].dt * 1000L, "M/d"
                )
            })"
        binding.includedLayoutHomeDaily.includeViewHomeDailyBottom.tvRain.text =
            String.format(context.getString(R.string.days), rainDay.toString())

        val hourTemp = mutableListOf<Int>()
        mutableListOf<ForestWeatherModel.ListDTO>()
        var hourLowTemp: Int? = null
        model.list.forEach {
            val temp = WeatherUtils.getTemp(it.temp.day).roundToInt()
            hourTemp.add(temp)
            if (null == hourLowTemp) {
                hourLowTemp = temp
            } else if (hourLowTemp!! > temp) {
                hourLowTemp = temp
            }
        }
    }

    private fun initTravel(holder: BaseViewHolder, model: WeatherModel?) {
        val binding = ItemAllLayoutHomeTravelBinding.bind(holder.itemView)
        val layoutP = binding.root.layoutParams
        if (model == null) {
            binding.root.gone()
            return
        }
        initCloth(holder, model)
        layoutP.height = ViewGroup.LayoutParams.WRAP_CONTENT
        binding.root.visible()
        val adapter = HomeTravelAdapter(R.layout.item_home_travel)
        val manager = LinearLayoutManager(context)
        binding.rvTravel.layoutManager = manager
        binding.rvTravel.adapter = adapter
        val list = mutableListOf<AdvModel>()
        val travel1: String
        val travel3: String
        when (WeatherUtils.getWeatherType(model.weather?.get(0)?.icon!!)) {
            BaseDrawer.Type.CLEAR_D, BaseDrawer.Type.CLEAR_N, BaseDrawer.Type.CLOUDY_D, BaseDrawer.Type.CLOUDY_N -> {
                travel1 = context.getString(R.string.travel_hint_1)
                travel3 = context.getString(R.string.travel_hint_3_a)
            }

            else -> {
                travel1 = context.getString(R.string.travel_hint_1_a)
                travel3 = context.getString(R.string.travel_hint_3)
            }
        }

        val travel2 = if (WeatherUtils.isColdSeason(model.dt!! * 1000L)) {
            context.getString(R.string.travel_hint_2_a)
        } else {
            context.getString(R.string.travel_hint_2)
        }

        list.add(
            AdvModel(travel1, R.drawable.ic_travel_1, null, null)
        )
        list.add(
            AdvModel(travel2, R.drawable.ic_travel_2, null, null)
        )
        list.add(
            AdvModel(travel3, R.drawable.ic_travel_3, null, null)
        )
        list.add(
            AdvModel(context.getString(R.string.travel_hint_4), R.drawable.ic_travel_4, null, null)
        )
        list.add(
            AdvModel(context.getString(R.string.travel_hint_5), R.drawable.ic_travel_5, null, null)
        )
        adapter.setList(list)
        adapter.setOnItemClickListener { _, _, _ ->

        }
    }

    private fun initAdv(holder: BaseViewHolder, model: WeatherModel?) {
        val binding = ItemAllLayoutHomeIndexBinding.bind(holder.itemView)
        val layoutP = binding.root.layoutParams
        if (model == null) {
            //layoutP.height = 0
            //binding.root.layoutParams = layoutP
            binding.root.gone()
            return
        }
        layoutP.height = ViewGroup.LayoutParams.WRAP_CONTENT
        binding.root.visible()
        val adapter = HomeAdvAdapter(R.layout.item_home_adv)
        val manager = GridLayoutManager(context, 2)
        binding.rvAdv.layoutManager = manager
        binding.rvAdv.adapter = adapter
        val list = mutableListOf<AdvModel>()
        var rainfall = 0
        if (model.rain?.h1 != null && model.rain!!.h1 != 0.0) {
            rainfall = 100
        } else if (model.rain?.h3 != null && model.rain!!.h3 != 0.0) {
            rainfall = 50
        }
        list.add(
            AdvModel(
                context.getString(R.string.rainfall_prediction),
                R.drawable.ic_uv_index,
                "$rainfall%",
                context.getString(R.string.rainfall_prediction_des)
            )
        )
        list.add(
            AdvModel(
                context.getString(R.string.wind_text),
                R.drawable.ic_index_wind,
                model.wind?.speed.toString() + "m/s",
                context.getString(R.string.wind_des)
            )
        )
        list.add(
            AdvModel(
                context.getString(R.string.humidity),
                R.drawable.ic_humidity,
                model.main?.humidity.toString() + "%",
                context.getString(R.string.humidity_des)
            )
        )
        list.add(
            AdvModel(
                context.getString(R.string.visibility),
                R.drawable.ic_visibility,
                String.format(context.getString(R.string.m_unit), model.visibility.toString()),
                context.getString(R.string.visibility_des)
            )
        )
        list.add(
            AdvModel(
                context.getString(R.string.pressure),
                R.drawable.ic_rainfall_prediction,
                ((model.main?.pressure ?: 0)).toString() + "hpa",
                context.getString(R.string.pressure_des)
            )
        )
        list.add(
            AdvModel(
                context.getString(R.string.precipitation),
                R.drawable.ic_precipitation,
                (model.rain?.h1 ?: 0).toString() + " mm",
                context.getString(R.string.precipitation_des)
            )
        )
        adapter.setList(list)
        adapter.setOnItemClickListener { _, _, _ ->

        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun initAirQuality(holder: BaseViewHolder, model: AirModel?) {
        val binding = ItemAllLayoutHomeAirBinding.bind(holder.itemView)
        val layoutP = binding.root.layoutParams
        if (model == null) {
            //layoutP.height = 0
            //binding.root.layoutParams = layoutP
            binding.root.gone()
            return
        }
        layoutP.height = ViewGroup.LayoutParams.WRAP_CONTENT
        binding.root.visible()
        val airs = context.resources.getStringArray(R.array.air_quality_list)
        val aqi = (model.list?.get(0)?.main?.aqi ?: 1)
        val airQualityStr = airs[aqi - 1]
        binding.includedLayoutHomeAir.tvAirDes.text = airQualityStr

        binding.includedLayoutHomeAir.ivAirBar.post {
            var left = (binding.includedLayoutHomeAir.ivAirBar.width / 5 * aqi) - 100
            if (aqi == 5) {
                left = (binding.includedLayoutHomeAir.ivAirBar.width / 5 * aqi) - ScreenUtils.dp2px(
                    context, 100
                )
            }
            val lp = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            lp.setMargins(left, 0, 0, 0)
            binding.includedLayoutHomeAir.llyAirFlag.layoutParams = lp
            val components = model.list?.get(0)?.components
            binding.includedLayoutHomeAir.tvAir.text = AqiUtil.CountAqi(
                components?.pm25 ?: 0.0,
                components?.pm10 ?: 0.0,
                components?.co ?: 0.0,
                components?.no2 ?: 0.0,
                components?.o3 ?: 0.0,
                components?.so2 ?: 0.0
            )?.aqi?.roundToInt().toString()
            val adapterAir = AirQualityAdapter(R.layout.item_air_quality)
            val manager = GridLayoutManager(context, 6)
            binding.includedLayoutHomeAir.rvAir.layoutManager = manager
            binding.includedLayoutHomeAir.rvAir.adapter = adapterAir
            adapterAir.setList(
                AqiUtil.CountAqiList(
                    components?.pm25 ?: 0.0,
                    components?.pm10 ?: 0.0,
                    components?.co ?: 0.0,
                    components?.no2 ?: 0.0,
                    components?.o3 ?: 0.0,
                    components?.so2 ?: 0.0
                )
            )
        }
    }

    private fun initSunriseSunset(holder: BaseViewHolder, model: WeatherModel?) {
        val binding = ItemAllLayoutHomeSunsetBinding.bind(holder.itemView)
        val layoutP = binding.root.layoutParams
        if (model == null) {
            //layoutP.height = 0
            //binding.root.layoutParams = layoutP
            binding.root.gone()
            return
        }
        layoutP.height = ViewGroup.LayoutParams.WRAP_CONTENT
        binding.root.visible()
        val sunrise = TimeUtils.getDateToString(model.sys!!.sunrise!! * 1000L, "HH:mm a")
        val sunset = TimeUtils.getDateToString(model.sys!!.sunset!! * 1000L, "HH:mm a")
        val durationH = (model.sys!!.sunset!! - model.sys!!.sunrise!!) / 60 / 60
        val durationM = (model.sys!!.sunset!! - model.sys!!.sunrise!!) / 60 % 60
        val duration = "${durationH}h ${durationM}m"
        binding.includedLayoutHomeSunset.tvDuration.text = duration
        binding.includedLayoutHomeSunset.tvSunrise.text = sunrise
        binding.includedLayoutHomeSunset.tvSunset.text = sunset
    }

    private fun initCloth(holder: BaseViewHolder, model: WeatherModel?) {
        val binding = ItemAllLayoutHomeClothBinding.bind(holder.itemView)
        val layoutP = binding.root.layoutParams
        if (model == null) {
            //layoutP.height = 0
            //binding.root.layoutParams = layoutP
            binding.root.gone()
            return
        }
        layoutP.height = ViewGroup.LayoutParams.WRAP_CONTENT
        binding.root.visible()
        when {
            getTempC(model.main?.temp) <= 0 -> {
                binding.tvDes.setText(R.string.clothing_index_cold_dex)
                binding.tvClothIndex.setText(R.string.clothing_index_cold)
            }

            getTempC(model.main?.temp).toInt() <= 10 -> {
                binding.tvDes.setText(R.string.clothing_index_cool_dex)
                binding.tvClothIndex.setText(R.string.clothing_index_cool)
            }

            getTempC(model.main?.temp).toInt() <= 20 -> {
                binding.tvDes.setText(R.string.clothing_index_moderate_dex)
                binding.tvClothIndex.setText(R.string.clothing_index_moderate)
            }

            getTempC(model.main?.temp).toInt() <= 30 -> {
                binding.tvDes.setText(R.string.clothing_index_warm_dex)
                binding.tvClothIndex.setText(R.string.clothing_index_warm)
            }

            else -> {
                binding.tvDes.setText(R.string.clothing_index_hot_dex)
                binding.tvClothIndex.setText(R.string.clothing_index_hot)
            }
        }
    }

    private fun getTempC(temp: Double?): Double {
        return if (temp == null) {
            0.0
        } else {
            temp - 273.15
        }
    }
}