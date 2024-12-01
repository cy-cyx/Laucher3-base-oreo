package com.lambdaweather.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.lambda.weather.R
import com.lambdaweather.data.model.Aqi
import java.math.BigDecimal
import java.util.Collections

object AqiUtil {
    /**
     * @param @param  aqi
     * @param @return
     * @return int   @throws
     * @Title: getPollutionDegree
     * @Description: 计算aqi值对应的等级
     */
    fun getPollutionDegree(aqi: Double): Int {
        var pollutionDegree = 1
        if (aqi <= 50) {
            pollutionDegree = 1
        } else if (aqi > 50 && aqi <= 100) {
            pollutionDegree = 2
        } else if (aqi > 100 && aqi <= 150) {
            pollutionDegree = 3
        } else if (aqi > 150 && aqi <= 200) {
            pollutionDegree = 4
        } else if (aqi > 200 && aqi <= 300) {
            pollutionDegree = 5
        } else if (aqi > 300) {
            pollutionDegree = 6
        }
        return pollutionDegree
    }

    /**
     * @param @param  pollutionDegree
     * @param @return
     * @return String   @throws
     * @Title: getDegree
     * @Description: 计算aqi值对应的等级
     */
    fun getDegree(pollutionDegree: Int): String {
        if (pollutionDegree == 1) {
            return "优"
        } else if (pollutionDegree == 2) {
            return "良"
        } else if (pollutionDegree == 3) {
            return "轻度污染"
        } else if (pollutionDegree == 4) {
            return "中度污染"
        } else if (pollutionDegree == 5) {
            return "重度污染"
        } else if (pollutionDegree == 6) {
            return "严重污染"
        }
        return "数据错误"
    }

    /**
     * 计算每种污染物项目 P的空气质量分指数
     *
     * @param cp 污染物项目P的质量浓度
     * @param r  污染物项目P所在数组中的行号
     * @return
     */
    fun countPerIaqi(cp: Double, r: Int): Double {
        var bph = 0.0 // 与 cp相近的污染物浓度限值的高位值
        var bpl = 0.0 // 与 cp相近的污染物浓度限值的低位值
        var iaqih = 0.0 // 与 bph对应的空气质量分指数
        var iaqil = 0.0 // 与 bpl对应的空气质量分指数
        var iaqip = 0.0 // 当前污染物项目P的空气质量分指数
        // 空气质量分指数及对应的污染物项目浓度限值
        val aqiArr = arrayOf(
            intArrayOf(0, 50, 100, 150, 200, 300, 400, 500),
            intArrayOf(0, 35, 75, 115, 150, 250, 350, 500),
            intArrayOf(0, 50, 150, 250, 350, 420, 500, 600),
            intArrayOf(0, 2, 4, 14, 24, 36, 48, 60),
            intArrayOf(0, 40, 80, 180, 280, 565, 750, 940),
            intArrayOf(0, 160, 200, 300, 400, 800, 1000, 1200),
            intArrayOf(0, 50, 150, 475, 800, 1600, 2100, 2620),
            intArrayOf(0, 100, 160, 215, 265, 800)
        )
        val min = aqiArr[r][0].toDouble()
        val index = aqiArr[r].size - 1
        val max = aqiArr[r][index].toDouble()
        return if (cp <= min || cp >= max) {
            0.0
        } else {
            // 对每种污染物的bph、bpl、iaqih、iaqil进行赋值
            for (i in r until r + 1) {
                for (j in aqiArr[0].indices) {
                    if (cp < aqiArr[i][j]) {
                        bph = aqiArr[i][j].toDouble()
                        bpl = aqiArr[i][j - 1].toDouble()
                        iaqih = aqiArr[0][j].toDouble()
                        iaqil = aqiArr[0][j - 1].toDouble()
                        break
                    }
                }
            }
            // 计算污染物项目 P的空气质量分指数
            iaqip = (iaqih - iaqil) / (bph - bpl) * (cp - bpl) + iaqil
            val bg = BigDecimal(Math.ceil(iaqip))
            bg.setScale(0, BigDecimal.ROUND_HALF_UP).toDouble()
        }
    }

    fun getCoIAQI(co: Double): Double {
        return if (co > 0) {
            countPerIaqi(co, 3)
        } else 0.0
    }

    fun getNo2IAQI(no2: Double): Double {
        if (no2 > 0) {
            val round = Math.round(no2)
            return countPerIaqi(round.toDouble(), 4)
        }
        return 0.0
    }

    fun getO3OneHourIAQI(o3One: Double): Double {
        if (o3One > 0) {
            val round = Math.round(o3One)
            return countPerIaqi(round.toDouble(), 5)
        }
        return 0.0
    }

    fun getO3EightHourIAQI(o3Eight: Double): Double {
        if (o3Eight > 0 && o3Eight <= 800) {
            val round = Math.round(o3Eight)
            return countPerIaqi(round.toDouble(), 7)
        }
        return 0.0
    }

    fun getPm10IAQI(pmte: Double): Double {
        if (pmte > 0) {
            val round = Math.round(pmte)
            return countPerIaqi(round.toDouble(), 2)
        }
        return 0.0
    }

    fun getPm25IAQI(pmtw: Double): Double {
        if (pmtw > 0) {
            val round = Math.round(pmtw)
            return countPerIaqi(round.toDouble(), 1)
        }
        return 0.0
    }

    fun getSo2IAQI(so2: Double): Double {
        if (so2 > 0) {
            val round = Math.round(so2)
            return countPerIaqi(round.toDouble(), 6)
        }
        return 0.0
    }

    /**
     * 根据提供污染物的各项指标，对AQI进行计算
     *
     * @param pmtw PM2.5
     * @param pmte PM10
     * @param co   一氧化碳浓度
     * @param no2  二氧化氮浓度
     * @param o3   臭氧浓度
     * @param so2  二氧化硫浓度
     * @return
     */
    fun CountAqi(
        pmtw: Double,
        pmte: Double,
        co: Double,
        no2: Double,
        o3: Double,
        so2: Double
    ): Aqi? {
        val pmtwIaqi = getPm25IAQI(pmtw)
        val pmteIaqi = getPm10IAQI(pmte)
        val coIaqi = getCoIAQI(co)
        val no2Iaqi = getNo2IAQI(no2)
        val o3Iaqi = getO3OneHourIAQI(o3)
        val so2Iaqi = getSo2IAQI(so2)
        // 初始化对象数组
        val aList: MutableList<Aqi> = ArrayList()
        if (pmtwIaqi != 0.0) {
            aList.add(Aqi("PM2.5", pmtwIaqi, getPollutionDegree(pmtwIaqi)))
        }
        if (pmteIaqi != 0.0) {
            aList.add(Aqi("PM10", pmteIaqi, getPollutionDegree(pmteIaqi)))
        }
        if (coIaqi != 0.0) {
            aList.add(Aqi("CO", coIaqi, getPollutionDegree(coIaqi)))
        }
        if (no2Iaqi != 0.0) {
            aList.add(Aqi("NO2", no2Iaqi, getPollutionDegree(no2Iaqi)))
        }
        if (o3Iaqi != 0.0) {
            aList.add(Aqi("O3", o3Iaqi, getPollutionDegree(o3Iaqi)))
        }
        if (so2Iaqi != 0.0) {
            aList.add(Aqi("SO2", so2Iaqi, getPollutionDegree(so2Iaqi)))
        }
        Collections.sort(aList, AqiComparator())
        var aqi: Aqi? = null
        if (aList.size > 0) {
            aqi = aList[aList.size - 1]
        }
        return aqi
    }

    fun CountAqiList(
        pmtw: Double,
        pmte: Double,
        co: Double,
        no2: Double,
        o3: Double,
        so2: Double
    ): List<Aqi> {
        val pmtwIaqi = getPm25IAQI(pmtw)
        val pmteIaqi = getPm10IAQI(pmte)
        val coIaqi = getCoIAQI(co)
        val no2Iaqi = getNo2IAQI(no2)
        val o3Iaqi = getO3OneHourIAQI(o3)
        val so2Iaqi = getSo2IAQI(so2)
        // 初始化对象数组
        val aList: MutableList<Aqi> = ArrayList()
        aList.add(Aqi("PM2.5", pmtwIaqi, getPollutionDegree(pmtwIaqi)))
        aList.add(Aqi("PM10", pmteIaqi, getPollutionDegree(pmteIaqi)))
        aList.add(Aqi("CO", coIaqi, getPollutionDegree(coIaqi)))
        aList.add(Aqi("NO2", no2Iaqi, getPollutionDegree(no2Iaqi)))
        aList.add(Aqi("O3", o3Iaqi, getPollutionDegree(o3Iaqi)))
        aList.add(Aqi("SO2", so2Iaqi, getPollutionDegree(so2Iaqi)))
        return aList
    }

    fun getAqiColor(context: Context?, aqiIndex: Int): Int {
        return when (aqiIndex) {
            1 -> ContextCompat.getColor(context!!, R.color.color_36ec76)
            2 -> ContextCompat.getColor(context!!, R.color.color_f9e351)
            3 -> ContextCompat.getColor(context!!, R.color.color_ea4c40)
            4 -> ContextCompat.getColor(context!!, R.color.color_bc34dc)
            5 -> ContextCompat.getColor(context!!, R.color.color_ff3829)
            else -> ContextCompat.getColor(context!!, R.color.color_971f1d)
        }
    }

    fun getAqiColorNum(context: Context?, aqiIndex: Int): Int {
        return getAqiColor(context, getPollutionDegree(aqiIndex.toDouble()))
    }

    /**
     * 构造分类器类，对AQI对象链表进行排序
     */
    internal class AqiComparator : Comparator<Any?> {
        override fun compare(o1: Any?, o2: Any?): Int {
            val a1 = o1 as Aqi?
            val a2 = o2 as Aqi?
            val result = a1!!.aqi - a2!!.aqi
            return result.toInt()
        }
    }
}
