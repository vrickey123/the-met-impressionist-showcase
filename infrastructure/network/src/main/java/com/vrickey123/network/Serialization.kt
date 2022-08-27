package com.vrickey123.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object Serialization {
    val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory()) // Order matters! Place Kotlin adapter last.
        .build()
}