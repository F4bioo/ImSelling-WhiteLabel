package com.fappslab.imselling.ui.fragment.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fappslab.imselling.domain.model.Product
import com.fappslab.imselling.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel
@Inject
constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _getProductsEvent = MutableLiveData<List<Product>>()
    val getProductsEvent: LiveData<List<Product>>
        get() = _getProductsEvent

    fun getProducts() = viewModelScope.launch {
        try {
            val products = getProductsUseCase()
            _getProductsEvent.value = products
        } catch (e: Exception) {

        }
    }
}