package com.fappslab.imselling.ui.products

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import com.fappslab.imselling.R
import com.fappslab.imselling.databinding.FragmentProductsBinding
import com.fappslab.imselling.domain.model.Product
import com.fappslab.imselling.utils.PRODUCT_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment(R.layout.fragment_products) {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProductsViewModel>()

    private val productsAdapter = ProductsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductsBinding.bind(view)
        initRecyclerView()
        initListeners()
        initObserveEvents()
        initObserverNavBackStack()
        getProducts()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initRecyclerView() {
        binding.recyclerProducts.run {
            setHasFixedSize(true)
            adapter = productsAdapter
        }
    }

    private fun initListeners() {
        with(binding) {
            swipeProducts.setOnRefreshListener {
                getProducts()
            }

            fabAdd.setOnClickListener {
                findNavController().navigate(R.id.action_productsFragment_to_addProductFragment)
            }
        }
    }

    private fun initObserveEvents() {
        viewModel.viewStateEvent.observe(viewLifecycleOwner) { viewState ->
            binding.flipperContainer.displayedChild = when (viewState) {
                is ProductsViewModel.ViewState.ShowProducts -> {
                    productsAdapter.submitList(viewState.products)
                    FLIPPER_POSITION_SUCCESS
                }
                is ProductsViewModel.ViewState.ShowError -> {
                    binding.textError.text = getString(viewState.messageResId)
                    FLIPPER_POSITION_ERROR
                }
            }
            binding.swipeProducts.isRefreshing = false
        }

        viewModel.addButtonVisibilityEvent.observe(viewLifecycleOwner) { isVisible ->
            binding.fabAdd.isVisible = isVisible
        }
    }

    private fun initObserverNavBackStack() {
        findNavController().run {
            val navBackStackEntry = getBackStackEntry(R.id.productsFragment)
            val savedStateHandle = navBackStackEntry.savedStateHandle
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME && savedStateHandle.contains(PRODUCT_KEY)) {
                    val product = savedStateHandle.get<Product>(PRODUCT_KEY)
                    val oldList = productsAdapter.currentList
                    val newList = oldList.toMutableList().apply {
                        add(product)
                    }
                    productsAdapter.submitList(newList)
                    binding.recyclerProducts.smoothScrollToPosition(newList.size - 1)
                    savedStateHandle.remove<Product>(PRODUCT_KEY)
                }
            }

            navBackStackEntry.lifecycle.addObserver(observer)

            viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    navBackStackEntry.lifecycle.removeObserver(observer)
                }
            })
        }
    }

    private fun getProducts() {
        viewModel.getProducts()
    }

    companion object {
        private const val FLIPPER_POSITION_ERROR = 0
        private const val FLIPPER_POSITION_SUCCESS = 1
    }
}
