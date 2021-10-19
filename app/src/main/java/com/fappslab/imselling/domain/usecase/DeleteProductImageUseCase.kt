package com.fappslab.imselling.domain.usecase

interface DeleteProductImageUseCase {

    suspend operator fun invoke(
        id: String
    ): String
}
