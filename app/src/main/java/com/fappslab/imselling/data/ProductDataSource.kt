package com.fappslab.imselling.data

import android.net.Uri
import com.fappslab.imselling.domain.model.Product

interface ProductDataSource {

    suspend fun getProducts(): List<Product>

    suspend fun uploadProductImage(id: String, imageUri: Uri): String

    suspend fun saveProduct(product: Product): Product

    suspend fun deleteProductImage(id: String): String

    suspend fun deleteProduct(product: Product): Product
}
