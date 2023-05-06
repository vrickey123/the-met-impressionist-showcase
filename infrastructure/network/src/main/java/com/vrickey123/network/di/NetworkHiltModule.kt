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
        okHttpClient: OkHttpClient,
        moshi: Moshi,
        @MetDBImpl metDatabase: MetDatabase,
    ): MetRepository {
        return MetRepositoryImpl(MetNetworkClient.create(okHttpClient, moshi), metDatabase, dispatcher)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
    ): OkHttpClient {
        return MetNetworkClient.buildOkHttpClient(context)
    }

    @Singleton
    @MetDBImpl
    @Provides
    fun provideMetDatabase(
        @ApplicationContext context: Context,
    ): MetDatabase {
        return MetDatabaseImpl.buildDatabase(context)
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory()) // Order matters! Place Kotlin adapter last.
            .build()
    }

    @Provides
    @IODispatcher
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}
