package com.vrickey123.network

import retrofit2.Response

/**
 * Convenience function for transforming an HTTP Response into a Kotlin [Result].
 * */
fun <T> Result.Companion.from(response: Response<T>): Result<T> {
    val body = response.body() // Kotlin smart cast is stupid
    return if (response.isSuccessful && body != null) {
        success(body)
    } else {
        failure(Throwable(response.message()))
    }
}