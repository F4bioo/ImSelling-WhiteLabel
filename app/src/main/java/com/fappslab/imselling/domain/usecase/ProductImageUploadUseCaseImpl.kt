package com.fappslab.imselling.domain.usecase

import android.net.Uri
import com.fappslab.imselling.data.ProductRepository

class ProductImageUploadUseCaseImpl(
    private val productRepository: ProductRepository
) : ProductImageUploadUseCase {

    override suspend fun invoke(imageUri: Uri): String {
        return productRepository.uploadProductImage(imageUri)
    }
}
