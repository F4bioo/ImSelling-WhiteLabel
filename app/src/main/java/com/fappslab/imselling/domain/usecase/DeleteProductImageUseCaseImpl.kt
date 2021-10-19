package com.fappslab.imselling.domain.usecase

import com.fappslab.imselling.data.ProductRepository
import javax.inject.Inject

class DeleteProductImageUseCaseImpl
@Inject
constructor(
    private val productRepository: ProductRepository
) : DeleteProductImageUseCase {

    override suspend fun invoke(
        id: String
    ): String {
        return try {
            productRepository.deleteProductImage(id)
        } catch (e: Exception) {
            throw e
        }
    }
}
