package com.lambdaweather.data.model

data class WidgetModel(
    val temp: String,
    val location: String,
    var type: Int,
    var id: Int? = null,
    val icon: String? = null
) {
    companion object {
        const val SUNNY = 0
        const val OVERCAST = 1
        const val NIGHT = 2
        const val EMPTY = 3
    }
}