package com.vrickey123.network.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.vrickey123.network.remote.MetNetworkClient
import com.vrickey123.network.MetRepository
import com.vrickey123.network.MetRepositoryImpl
import com.vrickey123.network.local.MetDatabase
import com.vrickey123.network.local.MetDatabaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MetRepoImpl

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MetDBImpl

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IODispatcher

@Module
@InstallIn(SingletonComponent::class)
object NetworkHiltModule {
    @Singleton
    @MetRepoImpl
    @Provides
    fun provideMetRepository(
        @IODispatcher dispatcher: CoroutineDispatcher,
        retrofit: Retrofit,
        @MetDBImpl metDatabase: MetDatabase,
    ): MetRepository = MetRepositoryImpl(
        MetNetworkClient.create(retrofit),
        metDatabase,
        dispatcher
    )

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
    ): OkHttpClient {
        val httpCache = MetNetworkClient.buildHttpCache(context)
        return MetNetworkClient.buildOkHttpClient(httpCache)
    }

    @Singleton
    @MetDBImpl
    @Provides
    fun provideMetDatabase(
        @ApplicationContext context: Context,
    ): MetDatabase = MetDatabaseImpl.buildDatabase(context)

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory()) // Order matters! Place Kotlin adapter last.
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit =
        MetNetworkClient.buildRetrofit(MetNetworkClient.BASE_URL, okHttpClient, moshi)

    @Provides
    @IODispatcher
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}
