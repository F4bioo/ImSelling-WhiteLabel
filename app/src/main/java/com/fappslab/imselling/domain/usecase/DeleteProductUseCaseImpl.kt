package com.fappslab.imselling.domain.usecase

import com.fappslab.imselling.data.ProductRepository
import com.fappslab.imselling.domain.model.Product
import javax.inject.Inject

class DeleteProductUseCaseImpl
@Inject
constructor(
    private val deleteProductImageUseCase: DeleteProductImageUseCase,
    private val productRepository: ProductRepository
) : DeleteProductUseCase {

    override suspend fun invoke(
        product: Product
    ): Product {
        return try {
            deleteProductImageUseCase.invoke(product.id)
            productRepository.deleteProduct(product)
        } catch (e: Exception) {
            throw e
        }
    }
}
