package com.fappslab.imselling.domain.usecase

import com.fappslab.imselling.domain.model.Product

interface DeleteProductUseCase {

    suspend operator fun invoke(
        product: Product
    ): Product
}
