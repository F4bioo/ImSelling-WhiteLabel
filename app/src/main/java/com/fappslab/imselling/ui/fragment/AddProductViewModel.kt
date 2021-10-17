package com.fappslab.imselling.ui.fragment

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fappslab.imselling.R
import com.fappslab.imselling.domain.usecase.CreateProductUseCase
import com.fappslab.imselling.utils.fromCurrency
import kotlinx.coroutines.launch

class AddProductViewModel(
    private val createProductUseCase: CreateProductUseCase
) : ViewModel() {

    private val _imageUriErrorResIdEvent = MutableLiveData<Int>()
    val imageUriErrorResIdEvent: LiveData<Int>
        get() = _imageUriErrorResIdEvent

    private val _inputDescriptionErrorResIdEvent = MutableLiveData<Int?>()
    val inputDescriptionErrorResIdEvent: LiveData<Int?>
        get() = _inputDescriptionErrorResIdEvent

    private val _inputPriceErrorResIdEvent = MutableLiveData<Int?>()
    val inputPriceErrorResIdEvent: LiveData<Int?>
        get() = _inputPriceErrorResIdEvent

    private var isFormValid = false

    fun createProduct(description: String, price: String, imageUri: Uri?) = viewModelScope.launch {
        isFormValid = true

        _imageUriErrorResIdEvent.value = getErrorDrawableResIdWhenNull(imageUri)
        _inputDescriptionErrorResIdEvent.value = getErrorStringResIdWhenEmpty(description)
        _inputPriceErrorResIdEvent.value = getErrorStringResIdWhenEmpty(price)

        if (isFormValid) {
            try {
                val product = createProductUseCase(description, price.fromCurrency(), imageUri!!)

            } catch (e: Exception) {
                println("<> CreateProduct: ${e.message}")
            }
        }
    }

    private fun getErrorDrawableResIdWhenNull(value: Uri?): Int {
        return value?.let {
            isFormValid = false
            R.drawable.background_product_image
        } ?: R.drawable.background_product_image_error
    }

    private fun getErrorStringResIdWhenEmpty(value: String): Int? {
        return if (value.trim().isEmpty()) {
            isFormValid = false
            R.string.add_product_field_error
        } else null
    }
}