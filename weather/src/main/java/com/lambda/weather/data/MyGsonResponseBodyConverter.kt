package com.lambdaweather.data

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset

/**
 * Author ZhengQinHong
 * Date 2017/3/27
 */
class MyGsonResponseBodyConverter<T>(private val mGson: Gson, private val adapter: TypeAdapter<T>) :
    Converter<ResponseBody, T> {
    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        val response = value.string()
        // response = convertStringFormat(response);
        val mediaType = value.contentType()
        val charset = if (mediaType != null) mediaType.charset(UTF_8) else UTF_8
        val bis = ByteArrayInputStream(response.toByteArray())
        val reader = InputStreamReader(bis, charset)
        val jsonReader = mGson.newJsonReader(reader)
        return try {
            adapter.read(jsonReader)
        } finally {
            value.close()
        }
    }

    companion object {
        private val UTF_8 = Charset.forName("UTF-8")
    }
}