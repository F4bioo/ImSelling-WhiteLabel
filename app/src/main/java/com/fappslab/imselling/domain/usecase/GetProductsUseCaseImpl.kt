package com.fappslab.imselling.domain.usecase

import com.fappslab.imselling.data.ProductRepository
import com.fappslab.imselling.domain.ErrorHandler
import com.fappslab.imselling.domain.model.Product
import com.fappslab.imselling.domain.type.ResultType
import javax.inject.Inject

class GetProductsUseCaseImpl
@Inject
constructor(
    private val productRepository: ProductRepository,
    private val errorHandler: ErrorHandler
) : GetProductsUseCase {

    override suspend fun invoke(): ResultType<List<Product>> {
        return try {
            val products = productRepository.getProducts()
            ResultType.Success(products)

        } catch (throwable: Throwable) {
            val error = errorHandler.getError(throwable)
            ResultType.Error(error)
        }
    }
}
