package com.fappslab.imselling.di

import com.fappslab.imselling.data.GeneralErrorHandlerImpl
import com.fappslab.imselling.domain.ErrorHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ErrorHandlerModule {

    @Singleton
    @Binds
    fun bindErrorHandler(
        errorHandlerImpl: GeneralErrorHandlerImpl
    ): ErrorHandler
}
