package com.fappslab.imselling.data

import android.net.Uri
import com.fappslab.imselling.domain.model.Product
import javax.inject.Inject

class ProductRepository
@Inject
constructor(
    private val dataSource: ProductDataSource
) {

    suspend fun getProducts(): List<Product> {
        return dataSource.getProducts()
    }

    suspend fun uploadProductImage(id: String, imageUri: Uri): String {
        return dataSource.uploadProductImage(id, imageUri)
    }

    suspend fun saveProduct(product: Product): Product {
        return dataSource.saveProduct(product)
    }

    suspend fun deleteProductImage(id: String): String {
        return dataSource.deleteProductImage(id)
    }

    suspend fun deleteProduct(product: Product): Product {
        return dataSource.deleteProduct(product)
    }
}
