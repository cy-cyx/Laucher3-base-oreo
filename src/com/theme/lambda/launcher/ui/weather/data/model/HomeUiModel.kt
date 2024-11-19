package com.lambdaweather.data.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.theme.lambda.launcher.ad.view.MRECBanner

data class HomeUiModel(
    override val itemType: Int,
    var newsModel: NewsModel? = null,
    var weatherModel: WeatherModel? = null,
    var forestWeatherModel: ForestWeatherModel? = null,
    var forestWeatherDay7Model: ForestDayWeatherModel? = null,
    var airModel: AirModel? = null,
    var isHide: Boolean? = null,
    var redCircle: Boolean? = null,
    var mrecBanner: MRECBanner? = null
) : MultiItemEntity {
    enum class HomeWidget(val type: Int) {
        NEWS(0),
        HOURLY(1),
        DAILY(2),
        AD(3),
        TRAVEL(4),
        INDEX(5),

        // AD(6) 小心有陷阱
        AIR(7),
        SUN(8),
    }
}