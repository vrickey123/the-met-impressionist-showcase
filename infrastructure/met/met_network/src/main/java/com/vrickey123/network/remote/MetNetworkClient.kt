package com.vrickey123.network.remote

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.vrickey123.met_api.MetObject
import com.vrickey123.met_api.MetSearchResult
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.File

interface MetNetworkClient {

    companion object {
        const val BASE_URL = "https://collectionapi.metmuseum.org"

        /** Creates a MetNetworkClient [Retrofit] instance with auto-serialization handled by
         * [Moshi] and networking with [OkHttpClient].
         * */
        fun create(retrofit: Retrofit): MetNetworkClient =
            retrofit.create(MetNetworkClient::class.java)

        fun buildRetrofit(baseUrl: String, okHttpClient: OkHttpClient, moshi: Moshi) =
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(okHttpClient)
                .build()

        fun buildOkHttpClient(httpCache: Cache): OkHttpClient = OkHttpClient.Builder()
            .cache(httpCache)
            .build()

        fun buildHttpCache(
            context: Context,
            cacheDir: File = File(context.cacheDir, "http_cache"),
            cacheSize: Long = 50L * 1024L * 1024L // 50 MiB
        ): Cache = Cache(cacheDir, cacheSize)

        fun buildMoshi(): Moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory()) // Order matters! Place Kotlin adapter last.
            .build()
    }

    @GET("/public/collection/v1/search")
    suspend fun search(
        @Query("q") query: String,
        @Query("hasImages") hasImages: Boolean,
        @Query("tags") tags: List<String>
    ): Response<MetSearchResult>

    @GET("public/collection/v1/objects/{objectID}")
    suspend fun fetchMetObject(@Path("objectID") objectID: Int): Response<MetObject>
}