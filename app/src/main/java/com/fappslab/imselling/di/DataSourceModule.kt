package com.fappslab.imselling.di

import com.fappslab.imselling.data.FirebaseProductDataSource
import com.fappslab.imselling.data.ProductDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    fun bindProductDataSource(
        dataSource: FirebaseProductDataSource
    ): ProductDataSource
}
