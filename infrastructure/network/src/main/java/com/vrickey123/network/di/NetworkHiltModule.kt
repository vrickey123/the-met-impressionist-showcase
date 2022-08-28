package com.vrickey123.network.di

import android.content.Context
import com.vrickey123.network.MetNetworkClient
import com.vrickey123.network.MetRepository
import com.vrickey123.network.MetRepositoryImpl
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
    ): MetRepository {
        return MetRepositoryImpl(MetNetworkClient.create(okHttpClient), dispatcher)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
    ): OkHttpClient {
        return MetNetworkClient.buildOkHttpClient(context)
    }

    @Provides
    @IODispatcher
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}
