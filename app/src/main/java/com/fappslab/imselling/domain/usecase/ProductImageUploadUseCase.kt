package com.fappslab.imselling.domain.usecase

import android.net.Uri

interface ProductImageUploadUseCase {

    suspend operator fun invoke(imageUri: Uri): String
}
