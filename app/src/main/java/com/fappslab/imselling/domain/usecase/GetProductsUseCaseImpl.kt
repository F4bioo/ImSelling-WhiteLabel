package com.fappslab.imselling.domain.usecase

import com.fappslab.imselling.data.ProductRepository
import com.fappslab.imselling.domain.model.Product
import javax.inject.Inject

class GetProductsUseCaseImpl
@Inject
constructor(
    private val productRepository: ProductRepository
) : GetProductsUseCase {

    override suspend fun invoke(): List<Product> {
        return productRepository.getProducts()
    }
}
