package com.fappslab.imselling.di

import com.fappslab.imselling.config.Config
import com.fappslab.imselling.config.ConfigImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ConfigModule {

    @Binds
    fun bindConfig(
        config: ConfigImpl
    ): Config
}
