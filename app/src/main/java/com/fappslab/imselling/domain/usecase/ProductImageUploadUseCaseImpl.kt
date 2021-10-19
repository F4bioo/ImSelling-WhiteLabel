package com.fappslab.imselling.domain.usecase

import android.net.Uri
import com.fappslab.imselling.data.ProductRepository
import javax.inject.Inject

class ProductImageUploadUseCaseImpl
@Inject
constructor(
    private val productRepository: ProductRepository
) : ProductImageUploadUseCase {

    override suspend fun invoke(id: String, imageUri: Uri): String {
        return productRepository.uploadProductImage(id, imageUri)
    }
}
