package com.theme.lambda.launcher.data.model

data class ForestDayWeather(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<ListDay>,
    val message: Double
) {
    data class City(
        val coord: Coord,
        val country: String,
        val id: Int,
        val name: String,
        val population: Int,
        val timezone: Int
    )

    data class ListDay(
        val clouds: Int,
        val deg: Int,
        val dt: Int,
        val feels_like: FeelsLike,
        val gust: Double,
        val humidity: Int,
        val pop: Double,
        val pressure: Int,
        val speed: Double,
        val sunrise: Int,
        val sunset: Int,
        val temp: Temp,
        val weather: List<Weather>
    )

    data class Coord(
        val lat: Int,
        val lon: Int
    )

    data class FeelsLike(
        val day: Double,
        val eve: Double,
        val morn: Double,
        val night: Double
    )

    data class Temp(
        val day: Double,
        val eve: Double,
        val max: Double,
        val min: Double,
        val morn: Double,
        val night: Double
    )

    data class Weather(
        val description: String,
        val icon: String,
        val id: Int,
        val main: String
    )
}

