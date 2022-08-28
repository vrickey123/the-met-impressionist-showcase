package com.vrickey123.router.di

import com.vrickey123.router.Router
import com.vrickey123.router.RouterImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MetRouterImpl

@Module
@InstallIn(SingletonComponent::class)
object RouterHiltModule {
    @Singleton
    @MetRouterImpl
    @Provides
    fun provideRouter(): Router {
        return RouterImpl()
    }
}