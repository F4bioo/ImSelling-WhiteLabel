package com.fappslab.imselling.data

import android.net.Uri
import com.fappslab.imselling.domain.model.Product
import javax.inject.Inject

class ProductRepository
@Inject
constructor(
    private val dataSource: ProductDataSource
) {

    suspend fun getProducts(): List<Product> =
        dataSource.getProducts()

    suspend fun uploadProductImage(imageUri: Uri): String =
        dataSource.uploadProductImage(imageUri)

    suspend fun createProducts(product: Product): Product =
        dataSource.createProducts(product)
}
