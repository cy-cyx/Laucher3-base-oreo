package com.lambdaweather.data.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName

class EarthModel {
    @SerializedName("type")
    val type: String? = null

    @SerializedName("metadata")
    val metadata: MetadataDTO? = null

    @SerializedName("features")
    val features: List<FeaturesDTO>? = null

    @SerializedName("bbox")
    val bbox: List<Double>? = null

    class MetadataDTO {
        @SerializedName("generated")
        val generated: Long? = null

        @SerializedName("url")
        val url: String? = null

        @SerializedName("title")
        val title: String? = null

        @SerializedName("status")
        val status: Int? = null

        @SerializedName("api")
        val api: String? = null

        @SerializedName("count")
        val count: Int? = null
    }

    class FeaturesDTO : MultiItemEntity {
        override val itemType: Int
            get() = 0

        @SerializedName("type")
        val type: String? = null

        @SerializedName("properties")
        val properties: PropertiesDTO? = null

        @SerializedName("geometry")
        val geometry: GeometryDTO? = null

        @SerializedName("id")
        val id: String? = null

        class PropertiesDTO {
            @SerializedName("mag")
            val mag: Double? = null

            @SerializedName("place")
            val place: String? = null

            @SerializedName("time")
            val time: Long? = null

            @SerializedName("updated")
            val updated: Long? = null

            @SerializedName("tz")
            val tz: String? = null

            @SerializedName("url")
            val url: String? = null

            @SerializedName("detail")
            val detail: String? = null

            @SerializedName("felt")
            val felt: String? = null

            @SerializedName("cdi")
            val cdi: String? = null

            @SerializedName("mmi")
            val mmi: String? = null

            @SerializedName("alert")
            val alert: String? = null

            @SerializedName("status")
            val status: String? = null

            @SerializedName("tsunami")
            val tsunami: Int? = null

            @SerializedName("sig")
            val sig: Int? = null

            @SerializedName("net")
            val net: String? = null

            @SerializedName("code")
            val code: String? = null

            @SerializedName("ids")
            val ids: String? = null

            @SerializedName("sources")
            val sources: String? = null

            @SerializedName("types")
            val types: String? = null

            @SerializedName("nst")
            val nst: Int? = null

            @SerializedName("dmin")
            val dmin: Double? = null

            @SerializedName("rms")
            val rms: Double? = null

            @SerializedName("gap")
            val gap: Double? = null

            @SerializedName("magType")
            val magType: String? = null

            @SerializedName("type")
            val type: String? = null

            @SerializedName("title")
            val title: String? = null
        }

        class GeometryDTO {
            @SerializedName("type")
            val type: String? = null

            @SerializedName("coordinates")
            val coordinates: List<Double>? = null
        }
    }
}