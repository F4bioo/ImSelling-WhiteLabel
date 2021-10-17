package com.fappslab.imselling.domain.usecase

import android.net.Uri
import com.fappslab.imselling.domain.model.Product

interface CreateProductUseCase {

    suspend operator fun invoke(
        title: String,
        description: String,
        price: Double,
        imageUri: Uri
    ): Product
}
