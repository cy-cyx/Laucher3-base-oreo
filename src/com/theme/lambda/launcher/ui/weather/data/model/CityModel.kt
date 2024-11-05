package com.lambdaweather.data.model

import com.google.gson.annotations.SerializedName
import com.lambdaweather.utils.LocalUtils
import java.lang.reflect.Field

class CityModel {

    @SerializedName("name")
    var name: String? = null

    @SerializedName("local_names")
    var localNames: LocalNamesDTO? = null

    @SerializedName("lat")
    var lat: Double? = null

    @SerializedName("lon")
    var lon: Double? = null

    @SerializedName("country")
    var country: String? = null

    @SerializedName("state")
    var state: String? = null

    var isLocation: Boolean? = null

    var title: String? = null

    var isNormal: Boolean? = false

    var isHideLine: Boolean? = false

    var isFirst: Boolean? = false

    fun getMyLocalName(): String {
        return try {
            var field: Field? = null
            if (null != localNames) {
                field = localNames!!::class.java.getDeclaredField(
                    LocalUtils.getCurrentLanguage().lowercase()
                )
                field.isAccessible = true
            }
            field?.get(localNames)?.toString() ?: this.name.toString()
        } catch (e: Exception) {
            name.toString()
        }
    }

    class LocalNamesDTO {
        @SerializedName("af")
        val af: String? = null

        @SerializedName("ro")
        val ro: String? = null

        @SerializedName("ka")
        val ka: String? = null

        @SerializedName("vi")
        val vi: String? = null

        @SerializedName("fj")
        val fj: String? = null

        @SerializedName("sl")
        val sl: String? = null

        @SerializedName("id")
        val id: String? = null

        @SerializedName("en")
        val en: String? = null

        @SerializedName("ki")
        val ki: String? = null

        @SerializedName("kn")
        val kn: String? = null

        @SerializedName("ms")
        val ms: String? = null

        @SerializedName("bs")
        val bs: String? = null

        @SerializedName("hi")
        val hi: String? = null

        @SerializedName("fa")
        val fa: String? = null

        @SerializedName("eu")
        val eu: String? = null

        @SerializedName("oc")
        val oc: String? = null

        @SerializedName("ml")
        val ml: String? = null

        @SerializedName("fr")
        val fr: String? = null

        @SerializedName("ne")
        val ne: String? = null

        @SerializedName("mg")
        val mg: String? = null

        @SerializedName("pa")
        val pa: String? = null

        @SerializedName("my")
        val my: String? = null

        @SerializedName("da")
        val da: String? = null

        @SerializedName("mi")
        val mi: String? = null

        @SerializedName("qu")
        val qu: String? = null

        @SerializedName("lt")
        val lt: String? = null

        @SerializedName("et")
        val et: String? = null

        @SerializedName("lb")
        val lb: String? = null

        @SerializedName("hu")
        val hu: String? = null

        @SerializedName("sq")
        val sq: String? = null

        @SerializedName("ca")
        val ca: String? = null

        @SerializedName("uz")
        val uz: String? = null

        @SerializedName("nn")
        val nn: String? = null

        @SerializedName("be")
        val be: String? = null

        @SerializedName("th")
        val th: String? = null

        @SerializedName("ln")
        val ln: String? = null

        @SerializedName("nl")
        val nl: String? = null

        @SerializedName("ja")
        val ja: String? = null

        @SerializedName("os")
        val os: String? = null

        @SerializedName("tn")
        val tn: String? = null

        @SerializedName("ar")
        val ar: String? = null

        @SerializedName("tl")
        val tl: String? = null

        @SerializedName("uk")
        val uk: String? = null

        @SerializedName("la")
        val la: String? = null

        @SerializedName("no")
        val no: String? = null

        @SerializedName("az")
        val az: String? = null

        @SerializedName("bn")
        val bn: String? = null

        @SerializedName("lo")
        val lo: String? = null

        @SerializedName("fo")
        val fo: String? = null

        @SerializedName("el")
        val el: String? = null

        @SerializedName("mr")
        val mr: String? = null

        @SerializedName("bg")
        val bg: String? = null

        @SerializedName("gl")
        val gl: String? = null

        @SerializedName("za")
        val za: String? = null

        @SerializedName("de")
        val de: String? = null

        @SerializedName("ga")
        val ga: String? = null

        @SerializedName("io")
        val io: String? = null

        @SerializedName("kk")
        val kk: String? = null

        @SerializedName("sr")
        val sr: String? = null

        @SerializedName("is")
        val `is`: String? = null

        @SerializedName("cs")
        val cs: String? = null

        @SerializedName("ny")
        val ny: String? = null

        @SerializedName("ug")
        val ug: String? = null

        @SerializedName("ce")
        val ce: String? = null

        @SerializedName("hy")
        val hy: String? = null

        @SerializedName("es")
        val es: String? = null

        @SerializedName("he")
        val he: String? = null

        @SerializedName("ur")
        val ur: String? = null

        @SerializedName("it")
        val it: String? = null

        @SerializedName("ky")
        val ky: String? = null

        @SerializedName("ko")
        val ko: String? = null

        @SerializedName("wo")
        val wo: String? = null

        @SerializedName("pl")
        val pl: String? = null

        @SerializedName("br")
        val br: String? = null

        @SerializedName("sk")
        val sk: String? = null

        @SerializedName("sw")
        val sw: String? = null

        @SerializedName("ku")
        val ku: String? = null

        @SerializedName("sv")
        val sv: String? = null

        @SerializedName("bm")
        val bm: String? = null

        @SerializedName("ha")
        val ha: String? = null

        @SerializedName("eo")
        val eo: String? = null

        @SerializedName("fi")
        val fi: String? = null

        @SerializedName("kw")
        val kw: String? = null

        @SerializedName("ru")
        val ru: String? = null

        @SerializedName("sh")
        val sh: String? = null

        @SerializedName("pt")
        val pt: String? = null

        @SerializedName("ta")
        val ta: String? = null

        @SerializedName("mn")
        val mn: String? = null

        @SerializedName("zh")
        val zh: String? = null

        @SerializedName("lv")
        val lv: String? = null

        @SerializedName("ie")
        val ie: String? = null

        @SerializedName("tt")
        val tt: String? = null

        @SerializedName("cy")
        val cy: String? = null

        @SerializedName("tr")
        val tr: String? = null

        @SerializedName("ba")
        val ba: String? = null

        @SerializedName("am")
        val am: String? = null

        @SerializedName("hr")
        val hr: String? = null

        @SerializedName("bo")
        val bo: String? = null
    }
}