package com.vrickey123.network.remote

import android.content.Context
import com.squareup.moshi.Moshi
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
        /** Creates a MetNetworkClient instance with auto-serialization handled by [Moshi]
         * networking with [OkHttpClient].
         *
         * The Dispatcher used by the internal OkHTTPClient is set to a default of 64 concurrent
         * requests, which should fall under the 80 requests per second rate limit of the Met API.
         * https://stackoverflow.com/questions/52881862/throttle-or-limit-kotlin-coroutine-count
         * */
        fun create(okHttpClient: OkHttpClient, moshi: Moshi): MetNetworkClient {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://collectionapi.metmuseum.org")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(okHttpClient)
                .build()

            return retrofit.create(MetNetworkClient::class.java)
        }

        fun buildOkHttpClient(context: Context): OkHttpClient {
            val cacheDir = File(context.cacheDir, "http_cache")
            val cacheSize = 50L * 1024L * 1024L // 50 MiB
            return OkHttpClient.Builder()
                .cache(Cache(cacheDir, cacheSize))
                .build()
        }
    }

    @GET("/public/collection/v1/search")
    suspend fun search(
        @Query("q") query: String,
        @Query("hasImages") hasImages: Boolean,
        @Query("tags") tags: List<String>
    ): Response<com.vrickey123.met_api.MetSearchResult>

    @GET("public/collection/v1/objects/{objectID}")
    suspend fun fetchMetObject(@Path("objectID") objectID: Int): Response<com.vrickey123.met_api.MetObject>
}