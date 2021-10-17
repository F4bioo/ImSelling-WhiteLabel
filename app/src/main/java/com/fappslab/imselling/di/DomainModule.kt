package com.fappslab.imselling.di

import com.fappslab.imselling.domain.usecase.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface DomainModule {

    @Binds
    fun bindCreateProductUseCase(
        useCase: CreateProductUseCaseImpl
    ): CreateProductUseCase

    @Binds
    fun bindGetProductsUseCase(
        useCase: GetProductsUseCaseImpl
    ): GetProductsUseCase

    @Binds
    fun bindProductImageUploadUseCase(
        useCase: ProductImageUploadUseCaseImpl
    ): ProductImageUploadUseCase
}
