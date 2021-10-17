package com.fappslab.imselling.ui.addproduct

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fappslab.imselling.R
import com.fappslab.imselling.domain.model.Product
import com.fappslab.imselling.domain.usecase.CreateProductUseCase
import com.fappslab.imselling.utils.fromCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel
@Inject constructor(
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

    private val _createProductEvent = MutableLiveData<Product>()
    val createProductEvent: LiveData<Product>
        get() = _createProductEvent

    private var isFormValid = false

    fun createProduct(description: String, price: String, imageUri: Uri?) = viewModelScope.launch {
        isFormValid = true

        _imageUriErrorResIdEvent.value = getErrorDrawableResIdWhenNull(imageUri)
        _inputDescriptionErrorResIdEvent.value = getErrorStringResIdWhenEmpty(description)
        _inputPriceErrorResIdEvent.value = getErrorStringResIdWhenEmpty(price)

        if (isFormValid) {
            try {
                val product = createProductUseCase(description, price.fromCurrency(), imageUri!!)
                _createProductEvent.value = product

            } catch (e: Exception) {
                println("<> AddProductViewModel: ${e.message}")
            }
        }
    }

    private fun getErrorDrawableResIdWhenNull(value: Uri?): Int {
        return if (value == null) {
            isFormValid = false
            R.drawable.background_product_image_error
        } else R.drawable.background_product_image
    }

    private fun getErrorStringResIdWhenEmpty(value: String): Int? {
        return if (value.trim().isEmpty()) {
            isFormValid = false
            R.string.add_product_field_error
        } else null
    }
}
