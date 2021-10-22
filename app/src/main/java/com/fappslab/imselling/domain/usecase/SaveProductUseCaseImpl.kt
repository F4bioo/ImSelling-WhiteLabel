package com.fappslab.imselling.domain.usecase

import android.net.Uri
import com.fappslab.imselling.data.ProductRepository
import com.fappslab.imselling.domain.model.Product
import java.util.*
import javax.inject.Inject

class SaveProductUseCaseImpl
@Inject
constructor(
    private val productImageUploadUseCase: ProductImageUploadUseCase,
    private val productRepository: ProductRepository
) : SaveProductUseCase {

    override suspend fun invoke(
        productId: String?,
        description: String,
        price: Double,
        imageUri: Uri
    ): Product {
        val id = productId ?: UUID.randomUUID().toString()
        return try {
            val imageUrl = productImageUploadUseCase(id, imageUri)
            val product = Product(
                id = id,
                description = description,
                price = price,
                imageUrl = imageUrl
            )
            productRepository.saveProduct(product)
        } catch (e: Exception) {
            throw e
        }
    }
}
