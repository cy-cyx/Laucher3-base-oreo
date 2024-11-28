package com.theme.lambda.launcher.utils

import android.text.TextUtils
import com.android.launcher3.R
import com.lambda.common.utils.CommonUtil
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object TimeUtil {

    fun getOldDate(distanceDay: Int): String? {
        val dft = SimpleDateFormat("yyyy-MM-dd")
        val beginDate = Date()
        val date = Calendar.getInstance()
        date.time = beginDate
        date.add(Calendar.DATE, distanceDay)
        var endDate: Date? = null
        try {
            endDate = dft.parse(dft.format(date.time))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return dft.format(endDate)
    }


    fun getData(): String {
        val calendar = Calendar.getInstance()
        return "${(calendar.get(Calendar.MONTH) + 1).toMouth()}${calendar.get(Calendar.DAY_OF_MONTH)}"
    }

    fun getWeek(): String {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_WEEK).toWeek()
    }

    fun getMouth(): String {
        val calendar = Calendar.getInstance()
        return (calendar.get(Calendar.MONTH) + 1).toMouth()
    }

    fun Int.toMouth() = when (this) {
        2 -> CommonUtil.getString(R.string.february)
        3 -> CommonUtil.getString(R.string.march)
        4 -> CommonUtil.getString(R.string.april)
        5 -> CommonUtil.getString(R.string.may)
        6 -> CommonUtil.getString(R.string.june)
        7 -> CommonUtil.getString(R.string.july)
        8 -> CommonUtil.getString(R.string.august)
        9 -> CommonUtil.getString(R.string.september)
        10 -> CommonUtil.getString(R.string.october)
        11 -> CommonUtil.getString(R.string.november)
        12 -> CommonUtil.getString(R.string.december)
        else -> CommonUtil.getString(R.string.january)
    }

    fun Int.toWeek() = when (this) {
        2 -> CommonUtil.getString(R.string.monday)
        3 -> CommonUtil.getString(R.string.tuesday)
        4 -> CommonUtil.getString(R.string.wednesday)
        5 -> CommonUtil.getString(R.string.thursday)
        6 -> CommonUtil.getString(R.string.friday)
        7 -> CommonUtil.getString(R.string.saturday)
        else -> CommonUtil.getString(R.string.sunday)
    }

    fun getCurrentMonthLastDay(): Int {
        val a = Calendar.getInstance()
        a[Calendar.DATE] = 1 //把日期设置为当月第一天
        a.roll(Calendar.DATE, -1) //日期回滚一天，也就是最后一天
        return a[Calendar.DATE]
    }

    fun getCurrentMonthStartWeek(): Int {
        val a = Calendar.getInstance()
        a[Calendar.DATE] = 1 //把日期设置为当月第一天
        return a[Calendar.DAY_OF_WEEK]
    }

    fun getCurrentDate(): Int {
        val a = Calendar.getInstance()
        return a[Calendar.DATE]
    }

    fun getDateToString(timeStamp: Long, format: String): String {
        var fm: String = format
        if (TextUtils.isEmpty(fm)) {
            fm = "yyyy-MM-dd HH:mm"
        }
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeStamp
        val sf = SimpleDateFormat(fm, Locale.getDefault())
        val date = sf.format(calendar.time)
        return date
    }

    fun getDayOfWeek(date : Date): String? {
        val dateFm = SimpleDateFormat("EEE")
        val currSun = dateFm.format(date)
        return currSun
    }
}