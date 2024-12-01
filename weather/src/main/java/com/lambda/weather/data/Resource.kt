package com.lambdaweather.data


sealed class Resource<T>(
    val data: T? = null,
    val appException: AppException? = null,
    var count: Int? = null
) {
    class Original<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Failure<T>(appException: AppException) : Resource<T>(null, appException)

    override fun toString(): String {
        return when (this) {
            is Original<T> -> "Original"
            is Success<*> -> "Success[data=$data]"
            is Failure -> "Failure[exception=$appException]"
            is Loading<T> -> "Loading"
        }
    }
}