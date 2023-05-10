package com.vrickey123.router.di

import com.vrickey123.router.Router
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RouterHiltModule {
    @Singleton
    @Provides
    fun provideRouter(): Router = Router()
}