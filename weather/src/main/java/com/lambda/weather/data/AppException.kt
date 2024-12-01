package com.lambdaweather.data

sealed class AppException(
    val errorCode: Int = 0,
    val errorMsg: String? = ""
) {
    class LocalException(errorMsg: String?) : AppException(errorMsg = errorMsg)
    class HttpException(errorMsg: String?) : AppException(errorMsg = errorMsg)
    class ServerException(errorCode: Int, errorMsg: String?) : AppException(errorCode, errorMsg)

    override fun toString(): String {
        return "${this.javaClass.name}: errorCode: $errorCode, errorMsg: $errorMsg"
    }
}
