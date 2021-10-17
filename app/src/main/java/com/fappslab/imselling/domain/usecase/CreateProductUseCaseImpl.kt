package com.fappslab.imselling.domain.usecase

import android.net.Uri
import com.fappslab.imselling.data.ProductRepository
import com.fappslab.imselling.domain.model.Product
import java.util.*

class CreateProductUseCaseImpl(
    private val productImageUploadUseCase: ProductImageUploadUseCase,
    private val productRepository: ProductRepository
) : CreateProductUseCase {
    override suspend fun invoke(
        description: String,
        price: Double,
        imageUri: Uri
    ): Product {
        return try {
            val imageUrl = productImageUploadUseCase(imageUri)
            val product = Product(
                UUID.randomUUID().toString(),
                description,
                price,
                imageUrl
            )
            productRepository.createProducts(product)
        } catch (e: Exception) {
            throw e
        }
    }
}
