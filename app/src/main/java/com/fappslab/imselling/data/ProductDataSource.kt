package com.fappslab.imselling.data

import android.net.Uri
import com.fappslab.imselling.domain.model.Product

interface ProductDataSource {

    suspend fun getProducts(): List<Product>

    suspend fun uploadProductImage(imageUri: Uri): String

    suspend fun createProducts(product: Product): Product
}
