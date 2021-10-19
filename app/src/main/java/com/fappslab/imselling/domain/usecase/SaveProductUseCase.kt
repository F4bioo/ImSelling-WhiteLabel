package com.fappslab.imselling.domain.usecase

import android.net.Uri
import com.fappslab.imselling.domain.model.Product

interface SaveProductUseCase {

    suspend operator fun invoke(
        productId: String?,
        description: String,
        price: Double,
        imageUri: Uri
    ): Product
}
