package com.fappslab.imselling.domain.usecase

import android.net.Uri

interface ProductImageUploadUseCase {

    suspend operator fun invoke(id: String, imageUri: Uri): String
}
