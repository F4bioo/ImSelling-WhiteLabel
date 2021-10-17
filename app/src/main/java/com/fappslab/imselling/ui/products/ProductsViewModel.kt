package com.fappslab.imselling.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fappslab.imselling.R
import com.fappslab.imselling.config.Config
import com.fappslab.imselling.domain.model.Product
import com.fappslab.imselling.domain.type.ErrorType
import com.fappslab.imselling.domain.type.ResultType
import com.fappslab.imselling.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel
@Inject
constructor(
    private val getProductsUseCase: GetProductsUseCase,
    config: Config
) : ViewModel() {

    private val _viewStateEvent = MutableLiveData<ViewState>()
    val viewStateEvent: LiveData<ViewState>
        get() = _viewStateEvent

    private val _addButtonVisibilityEvent = MutableLiveData(config.addButtonVisibility)
    val addButtonVisibilityEvent: LiveData<Boolean>
        get() = _addButtonVisibilityEvent

    fun getProducts() = viewModelScope.launch {
        _viewStateEvent.value = when (val result = getProductsUseCase()) {
            is ResultType.Success -> ViewState.ShowProducts(result.data)
            is ResultType.Error -> {
                ViewState.ShowError(
                    when (result.error) {
                        is ErrorType.AccessDenied -> R.string.products_error_access_denied
                        else -> R.string.products_error_access_unknown
                    }
                )
            }
        }
    }

    sealed class ViewState {
        class ShowProducts(val products: List<Product>) : ViewState()
        class ShowError(val messageResId: Int) : ViewState()
    }
}
