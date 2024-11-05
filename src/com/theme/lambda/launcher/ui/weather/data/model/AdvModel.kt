package com.lambdaweather.data.model

data class AdvModel(
    val name: String,
    val icon: Int,
    val content: String? = null,
    val des: String? = null
)