package com.theme.lambda.launcher.utils

import android.text.TextUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToLong


object TimeUtils {
    const val SECOND = 1L
    const val MINUTE = 60 * SECOND
    const val HOUR = 60 * MINUTE
    const val DAY = 24 * HOUR


    const val TIME_TYPE = "yyyy-MM-dd"
    const val TIME_TYPE2 = "yyyy-MM-dd HH:mm"

    /** 格式化日期的标准字符串  */
    private const val FORMAT = "yyyy-MM-dd HH:mm"

    /**
     * 毫秒转成秒
     * @return
     */
    fun millisecondTransSecond(millisecond: Long): Long {
        return (millisecond / 1000).toDouble().roundToLong()
    }

    /**
     * 获取当前时间戳
     * @return 时间戳
     */
    fun getCurrentTimeMillis(): Long {
        return System.currentTimeMillis()
    }

    /**
     * 获取当前时间戳-秒
     * @return 时间戳
     */
    fun getCurrentTimeSecond(): Long {
        return (System.currentTimeMillis() / 1000).toDouble().roundToLong()
    }

    /**
     * 秒转毫秒
     * @return 时间戳
     */
    fun SecondToMillis(millisecond: Long): Long {
        return (millisecond * 1000).toDouble().roundToLong()
    }

    /**
     * 获取当前日期时间
     * @return
     */
    fun getCurrentTime(): Date {
        return Date() //当前时间
    }

    /**
     * 得到某年某周的第一天
     *
     * @param year
     * @param week
     * @return
     */
    fun getFirstDayOfWeek(year: Int, week: Int): Date? {
        var week = week
        week = week - 1
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = Calendar.JANUARY
        calendar[Calendar.DATE] = 1
        val cal = calendar.clone() as Calendar
        cal.add(Calendar.DATE, week * 7)
        return getFirstDayOfWeek(cal.time)
    }

    /**
     * 得到某年某周的最后一天
     *
     * @param year
     * @param week
     * @return
     */
    fun getLastDayOfWeek(year: Int, week: Int): Date? {
        var week = week
        week = week - 1
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = Calendar.JANUARY
        calendar[Calendar.DATE] = 1
        val cal = calendar.clone() as Calendar
        cal.add(Calendar.DATE, week * 7)
        return getLastDayOfWeek(cal.time)
    }


    /**
     * 取得当前日期所在周的第一天
     *
     * @param date
     * @return
     */
    fun getFirstDayOfWeek(date: Date?): Date? {
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.SUNDAY
        calendar.time = date
        calendar[Calendar.DAY_OF_WEEK] = calendar.firstDayOfWeek // Sunday
        return calendar.time
    }

    /**
     * 取得当前日期所在周的最后一天
     *
     * @param date
     * @return
     */
    fun getLastDayOfWeek(date: Date?): Date? {
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.SUNDAY
        calendar.time = date
        calendar[Calendar.DAY_OF_WEEK] = calendar.firstDayOfWeek + 6 // Saturday
        return calendar.time
    }

    /**
     * 取得当前日期所在周的前一周最后一天
     *
     * @param date
     * @return
     */
    fun getLastDayOfLastWeek(date: Date?): Date? {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return getLastDayOfWeek(
            calendar[Calendar.YEAR],
            calendar[Calendar.WEEK_OF_YEAR] - 1
        )
    }

    /**
     * 返回指定日期的月的第一天
     * @param date
     * @return
     */
    fun getFirstDayOfMonth(date: Date?): Date? {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[calendar[Calendar.YEAR], calendar[Calendar.MONTH]] = 1
        return calendar.time
    }

    /**
     * 返回指定年月的月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    fun getFirstDayOfMonth(year: Int?, month: Int?): Date? {
        var year = year
        var month = month
        val calendar = Calendar.getInstance()
        if (year == null) {
            year = calendar[Calendar.YEAR]
        }
        if (month == null) {
            month = calendar[Calendar.MONTH]
        }
        calendar[year, month] = 1
        return calendar.time
    }

    /**
     * 返回指定日期的月的最后一天
     * @param date
     * @return
     */
    fun getLastDayOfMonth(date: Date?): Date? {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[calendar[Calendar.YEAR], calendar[Calendar.MONTH]] = 1
        calendar.roll(Calendar.DATE, -1)
        return calendar.time
    }

    /**
     * 返回指定年月的月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    fun getLastDayOfMonth(year: Int?, month: Int?): Date? {
        var year = year
        var month = month
        val calendar = Calendar.getInstance()
        if (year == null) {
            year = calendar[Calendar.YEAR]
        }
        if (month == null) {
            month = calendar[Calendar.MONTH]
        }
        calendar[year, month] = 1
        calendar.roll(Calendar.DATE, -1)
        return calendar.time
    }

    /**
     * 返回指定日期的上个月的最后一天
     * @param date
     * @return
     */
    fun getLastDayOfLastMonth(date: Date?): Date? {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[calendar[Calendar.YEAR], calendar[Calendar.MONTH] - 1] = 1
        calendar.roll(Calendar.DATE, -1)
        return calendar.time
    }

    /**
     * 返回指定日期的季的第一天
     * @param date
     * @return
     */
    fun getFirstDayOfQuarter(date: Date?): Date? {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return getFirstDayOfQuarter(
            calendar[Calendar.YEAR],
            getQuarterOfYear(date)
        )
    }

    /**
     * 返回指定年季的季的第一天
     *
     * @param year
     * @param quarter
     * @return
     */
    fun getFirstDayOfQuarter(year: Int?, quarter: Int): Date? {
        val calendar = Calendar.getInstance()
        var month = 0
        month = if (quarter == 1) {
            1 - 1
        } else if (quarter == 2) {
            4 - 1
        } else if (quarter == 3) {
            7 - 1
        } else if (quarter == 4) {
            10 - 1
        } else {
            calendar[Calendar.MONTH]
        }
        return getFirstDayOfMonth(year, month)
    }

    /**
     * 返回指定日期的季的最后一天
     * @param date
     * @return
     */
    fun getLastDayOfQuarter(date: Date?): Date? {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return getLastDayOfQuarter(
            calendar[Calendar.YEAR],
            getQuarterOfYear(date)
        )
    }

    /**
     * 返回指定年季的季的最后一天
     *
     * @param year
     * @param quarter
     * @return
     */
    fun getLastDayOfQuarter(year: Int?, quarter: Int): Date? {
        val calendar = Calendar.getInstance()
        var month = 0
        month = if (quarter == 1) {
            3 - 1
        } else if (quarter == 2) {
            6 - 1
        } else if (quarter == 3) {
            9 - 1
        } else if (quarter == 4) {
            12 - 1
        } else {
            calendar[Calendar.MONTH]
        }
        return getLastDayOfMonth(year, month)
    }

    /**
     * 返回指定日期的上一季的最后一天
     * @param date
     * @return
     */
    fun getLastDayOfLastQuarter(date: Date?): Date? {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return getLastDayOfLastQuarter(
            calendar[Calendar.YEAR],
            getQuarterOfYear(date)
        )
    }

    /**
     * 返回指定年季的上一季的最后一天
     *
     * @param year
     * @param quarter
     * @return
     */
    fun getLastDayOfLastQuarter(year: Int?, quarter: Int): Date? {
        val calendar = Calendar.getInstance()
        var month = 0
        month = if (quarter == 1) {
            12 - 1
        } else if (quarter == 2) {
            3 - 1
        } else if (quarter == 3) {
            6 - 1
        } else if (quarter == 4) {
            9 - 1
        } else {
            calendar[Calendar.MONTH]
        }
        return getLastDayOfMonth(year, month)
    }

    /**
     * 返回指定日期的季度
     *
     * @param date
     * @return
     */
    fun getQuarterOfYear(date: Date?): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.MONTH] / 3 + 1
    }

    /**
     * 获取当年的第一天
     * @return
     */
    fun getCurrYearFirst(): Date? {
        val currCal = Calendar.getInstance()
        val currentYear = currCal[Calendar.YEAR]
        return getYearFirst(currentYear)
    }

    /**
     * 获取当年的最后一天
     * @return
     */
    fun getCurrYearLast(): Date? {
        val currCal = Calendar.getInstance()
        val currentYear = currCal[Calendar.YEAR]
        return getYearLast(currentYear)
    }

    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    fun getYearFirst(year: Int): Date? {
        val calendar = Calendar.getInstance()
        calendar.clear()
        calendar[Calendar.YEAR] = year
        return calendar.time
    }

    /**
     * 获取某年最后一天日期
     * @param year 年份
     * @return Date
     */
    fun getYearLast(year: Int): Date? {
        val calendar = Calendar.getInstance()
        calendar.clear()
        calendar[Calendar.YEAR] = year
        calendar.roll(Calendar.DAY_OF_YEAR, -1)
        return calendar.time
    }

    /**
     * 将GMT Unix时间戳转换为系统默认时区的Unix时间戳
     * @param gmtUnixTime GMT Unix时间戳
     * @return 系统默认时区的Unix时间戳
     */
    fun getCurrentTimeZoneUnixTime(gmtUnixTime: Long): Long {
        return gmtUnixTime + TimeZone.getDefault().rawOffset
    }

    /**
     * 将GMT日期格式化为系统默认时区的日期字符串表达形式
     * @param gmtUnixTime GTM时间戳
     * @return 日期字符串"yyyy-MM-dd HH:mm:ss"
     */
    fun formatGMTUnixTime(gmtUnixTime: Long): String? {
        return formatGMTUnixTime(gmtUnixTime, FORMAT)
    }

    /**
     * 将GMT日期格式化为系统默认时区的日期字符串表达形式
     * @param gmtUnixTime GTM时间戳
     * @param format 格式化字符串
     * @return 日期字符串"yyyy-MM-dd HH:mm:ss"
     */
    fun formatGMTUnixTime(gmtUnixTime: Long, format: String?): String? {
        val dateFormat = SimpleDateFormat(format)
        return dateFormat.format(gmtUnixTime)
    }


    /**
     * 日期转化为字符串
     * @param data
     * @return
     */
    fun dataToString(data: Date?): String? {
        val format = SimpleDateFormat(TIME_TYPE)
        return format.format(data)
    }

    fun dataToString2(data: Date?, type: String): String? {
        val format = SimpleDateFormat(type)
        return format.format(data)
    }

    fun stringToDate(time: String, format: String): Date {
        val sdf = SimpleDateFormat(format)
        return sdf.parse(time)
    }

    fun longToDate(time: Long): Date {
        return Date(time)
    }

    fun getDateToString(timeStamp: Long, format: String): String {
        var fm: String = format
        if (TextUtils.isEmpty(fm)) {
            fm = "yyyy-MM-dd HH:mm"
        }
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeStamp
        //mss即是从服务器获取的时间戳
        val sf = SimpleDateFormat(fm, Locale.ENGLISH)
        val date = sf.format(calendar.time)
        return date
    }


    fun secToTime(time: Long): String {
        var timeStr: String? = null
        var hour: Long = 0
        var minute: Long = 0
        var second: Long = 0
        if (time <= 0) return "00:00" else {
            minute = time / 60
            if (minute < 60) {
                second = time % 60
                timeStr = unitFormat(minute) + ":" + unitFormat(second)
            } else {
                hour = minute / 60
                // if (hour > 99) return "99:59:59"
                minute = time / 60 % 60
                second = time % 60
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second)
            }
        }
        return timeStr
    }

    fun unitFormat(i: Long): String {
        var retStr: String? = null
        retStr = if (i in 0..9) "0$i" else "" + i
        return retStr
    }

    /**
     * 取得当前日期是星期几
     *
     * @param date
     * @return
     */
    fun getCurrentDayOfWeek(): String? {
        val date = Date()
        val dateFm = SimpleDateFormat("EEEE")
        val currSun = dateFm.format(date)
        return currSun
    }

    fun getDayOfWeek(date : Date): String? {
        val dateFm = SimpleDateFormat("EEE")
        val currSun = dateFm.format(date)
        return currSun
    }

    /**
     * 取得当前日期前几天是星期几
     *
     * @param date
     * @return
     */
    fun getDayOfWeeked(day: Int): String? {
        val dNow = Date() //当前时间

        var dBefore = Date()
        val calendar = Calendar.getInstance() //得到日历

        calendar.time = dNow //把当前时间赋给日历

        calendar.add(Calendar.DAY_OF_MONTH, day) //设置为前一天

        dBefore = calendar.time //得到前一天的时间
        val dateFm = SimpleDateFormat("EEEE")
        val currSun = dateFm.format(dBefore)
        when (currSun) {
            "Monday" -> {
                return "Mon"
            }
            "Tuesday" -> {
                return "Tues"
            }
            "Wednesday" -> {
                return "Wed"
            }
            "Thursday" -> {
                return "Thu"
            }
            "Friday" -> {
                return "Fri"
            }
            "Saturday" -> {
                return "Sat"
            }
            "Sunday" -> {
                return "Sun"
            }
        }
        return currSun
    }

    /**
     * 取得当前日期是星期几
     *
     * @param date
     * @return
     */
    fun getDayOfWeeked(dNow: Date): Int {
        val dateFm = SimpleDateFormat("EEEE")
        val currSun = dateFm.format(dNow)
        when (currSun) {
            "Monday" -> {
                return 1
            }
            "Tuesday" -> {
                return 2
            }
            "Wednesday" -> {
                return 3
            }
            "Thursday" -> {
                return 4
            }
            "Friday" -> {
                return 5
            }
            "Saturday" -> {
                return 6
            }
            "Sunday" -> {
                return 7
            }
        }
        return 1
    }


    /**
     * 取得当月天数
     */
    fun getCurrentMonthLastDay(): Int {
        val a = Calendar.getInstance()
        a[Calendar.DATE] = 1 //把日期设置为当月第一天
        a.roll(Calendar.DATE, -1) //日期回滚一天，也就是最后一天
        return a[Calendar.DATE]
    }

    fun timeSimple(time: Long): String {
        return if (time < 10) {
            "0${time}"
        } else {
            time.toString()
        }
    }

    fun stringTimeStamp(date: String?, format: String?): Long {
        try {
            val sdf = SimpleDateFormat(format)
            return (sdf.parse(date).time / 1000)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0L
    }

    fun getOldDate(distanceDay: Int): String? {
        val dft = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val beginDate = Date()
        val date = Calendar.getInstance()
        date.time = beginDate
        date[Calendar.DATE] = date[Calendar.DATE] + distanceDay
        var endDate: Date? = null
        try {
            endDate = dft.parse(dft.format(date.time))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return dft.format(endDate)
    }
}