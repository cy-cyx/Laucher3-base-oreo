package com.lambda.news.data

import com.lambda.common.utils.SpKey
import com.lambda.common.utils.getSpString
import com.lambda.common.utils.putSpString
import com.lambda.news.R
import com.lambda.news.data.model.LanguageBean
import com.lambda.news.data.model.CountryBean
import java.util.Locale

object LocalManager {

    val allCountryBeans = arrayListOf(
        CountryBean(
            "us",
            R.drawable.ic_country_united_states_of_america,
            R.string.united_states
        ), // 美国
        CountryBean("ru", R.drawable.ic_country_russia, R.string.russia),// 尔罗斯
        CountryBean("gb", R.drawable.ic_country_united_kingdom, R.string.united_kingdom),// 英国
        CountryBean("mx", R.drawable.ic_country_mexico, R.string.mexico),// 墨西哥
        CountryBean("ph", R.drawable.ic_country_philippines, R.string.philippines),// 菲律宾
        CountryBean("id", R.drawable.ic_country_indonesia, R.string.indonesia),// 印尼
        CountryBean("fr", R.drawable.ic_country_france, R.string.france),// 法国
        CountryBean("pl", R.drawable.ic_country_poland, R.string.poland),// 波兰
        CountryBean("nl", R.drawable.ic_country_netherlands, R.string.netherland),// 荷兰
    )

    val allLanguageBeans = arrayListOf(
        LanguageBean("en", R.string.english), // 英语
        LanguageBean("ru", R.string.russian),// 尔罗斯
        LanguageBean("es", R.string.spanish), //西班牙
        LanguageBean("fr", R.string.french),//法语
        LanguageBean("polish", R.string.polish), // 波兰语
        LanguageBean("nl", R.string.dutch),// 荷兰语
        LanguageBean("portuguese", R.string.portuguese), // 葡萄牙
        LanguageBean("indonesian", R.string.indonesian), // 印尼语
        LanguageBean("filipino", R.string.filipino),// 菲律宾语
    )

    fun getNewsCountry(): String {
        val country = SpKey.keyNewsCountry.getSpString()
        if (country.isNotBlank()) {
            return country
        }

        val localeCountry = Locale.getDefault().country.toLowerCase()
        val countryBean = allCountryBeans.find { it.countryCode == localeCountry }
        return countryBean?.countryCode ?: "us"
    }

    fun setNewsCountry(string: String) {
        SpKey.keyNewsCountry.putSpString(string)
    }

    fun getNewsCountryName(): Int {
        val country = getNewsCountry()
        val countryBean = allCountryBeans.find { it.countryCode == country }
        return countryBean?.country ?: R.string.united_states
    }

    fun getNewsCountryBean(): CountryBean {
        val country = getNewsCountry()
        return allCountryBeans.find { it.countryCode == country } ?: CountryBean(
            "us",
            R.drawable.ic_country_united_states_of_america,
            R.string.united_states
        )
    }

    fun getNewsLanguage(): String {
        val language = SpKey.keyNewsLanguage.getSpString()
        if (language.isNotBlank()) {
            return language
        }

        return when (getNewsCountry()) {
            "ru" -> "ru"
            "mx" -> "es"
            "ph" -> "filipino"
            "id" -> "indonesian"
            "fr" -> "fr"
            "pl" -> "polish"
            "nl" -> "nl"
            else -> "en"
        }
    }

    fun getNewSLanguageBean(): LanguageBean {
        val language = getNewsLanguage()
        return allLanguageBeans.find { it.languageCode == language } ?: LanguageBean(
            "en",
            R.string.english
        )
    }

    fun setNewsLanguage(string: String) {
        SpKey.keyNewsLanguage.putSpString(string)
    }

}