package com.fappslab.imselling.domain.usecase

import com.fappslab.imselling.domain.model.Product

interface GetProductsUseCase {

    suspend operator fun invoke(): List<Product>
}
