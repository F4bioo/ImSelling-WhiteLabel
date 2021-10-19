package com.fappslab.imselling.ui.saveproduct

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fappslab.imselling.R
import com.fappslab.imselling.domain.model.Product
import com.fappslab.imselling.domain.usecase.SaveProductUseCase
import com.fappslab.imselling.utils.fromCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveProductViewModel
@Inject constructor(
    private val saveProductUseCase: SaveProductUseCase
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

    private val _saveProductEvent = MutableLiveData<Product>()
    val saveProductEvent: LiveData<Product>
        get() = _saveProductEvent

    private var isFormValid = false

    fun saveProduct(productId: String?, description: String, price: String, imageUri: Uri?) =
        viewModelScope.launch {
            isFormValid = true

            _imageUriErrorResIdEvent.value = getErrorDrawableResIdWhenNull(imageUri)
            _inputDescriptionErrorResIdEvent.value = getErrorStringResIdWhenEmpty(description)
            _inputPriceErrorResIdEvent.value = getErrorStringResIdWhenEmpty(price)

            if (isFormValid) {
                try {
                    val product =
                        saveProductUseCase(productId, description, price.fromCurrency(), imageUri!!)
                    _saveProductEvent.value = product

                } catch (e: Exception) {
                    println("<> saveProduct: ${e.message}")
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
            R.string.save_product_field_error
        } else null
    }
}
