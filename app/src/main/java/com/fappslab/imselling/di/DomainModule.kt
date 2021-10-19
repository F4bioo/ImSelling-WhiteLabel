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
    fun bindSaveProductUseCase(
        useCase: SaveProductUseCaseImpl
    ): SaveProductUseCase

    @Binds
    fun bindDeleteProductUseCase(
        useCase: DeleteProductUseCaseImpl
    ): DeleteProductUseCase

    @Binds
    fun bindDeleteProductImageUseCase(
        useCase: DeleteProductImageUseCaseImpl
    ): DeleteProductImageUseCase

    @Binds
    fun bindGetProductsUseCase(
        useCase: GetProductsUseCaseImpl
    ): GetProductsUseCase

    @Binds
    fun bindProductImageUploadUseCase(
        useCase: ProductImageUploadUseCaseImpl
    ): ProductImageUploadUseCase
}
