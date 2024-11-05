package com.lambdaweather.data.model

import com.google.gson.annotations.SerializedName

class AirModel {

 @SerializedName("coord")
 val coord: CoordDTO? = null

 @SerializedName("list")
 val list: List<ListDTO>? = null

 class CoordDTO {
  @SerializedName("lon")
  val lon: Double? = null

  @SerializedName("lat")
  val lat: Double? = null
 }

 class ListDTO {
  @SerializedName("main")
  val main: MainDTO? = null

  @SerializedName("components")
  val components: ComponentsDTO? = null

  @SerializedName("dt")
  val dt: Int? = null

  var dtStr: String? = null

  class MainDTO {
   @SerializedName("aqi")
   val aqi: Int? = null
  }

  class ComponentsDTO {
   @SerializedName("co")
   val co: Double? = null

   @SerializedName("no")
   val no: Double? = null

   @SerializedName("no2")
   val no2: Double? = null

   @SerializedName("o3")
   val o3: Double? = null

   @SerializedName("so2")
   val so2: Double? = null

   @SerializedName("pm2_5")
   val pm25: Double? = null

   @SerializedName("pm10")
   val pm10: Double? = null

   @SerializedName("nh3")
   val nh3: Double? = null
  }
 }
}
