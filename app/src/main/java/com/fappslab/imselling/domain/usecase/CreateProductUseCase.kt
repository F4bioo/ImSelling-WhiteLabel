package com.fappslab.imselling.domain.usecase

import android.net.Uri
import com.fappslab.imselling.domain.model.Product

interface CreateProductUseCase {

    suspend operator fun invoke(
        tile: String,
        description: String,
        price: Double,
        imageUri: Uri
    ): Product
}
