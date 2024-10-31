package com.theme.lambda.launcher.utils

import com.android.launcher3.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

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

}