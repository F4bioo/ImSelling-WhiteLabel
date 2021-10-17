package com.fappslab.imselling.di

import com.fappslab.imselling.config.Config
import com.fappslab.imselling.config.ConfigImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface ConfigModule {

    @Binds
    fun bindConfig(
        config: ConfigImpl
    ): Config
}
