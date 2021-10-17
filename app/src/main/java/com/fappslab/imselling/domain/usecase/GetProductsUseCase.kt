package com.fappslab.imselling.domain.usecase

import com.fappslab.imselling.domain.model.Product
import com.fappslab.imselling.domain.type.ResultType

interface GetProductsUseCase {

    suspend operator fun invoke(): ResultType<List<Product>>
}
