package com.fappslab.imselling.ui.products

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fappslab.imselling.R
import com.fappslab.imselling.databinding.FragmentProductsBinding
import com.fappslab.imselling.domain.model.Product
import com.fappslab.imselling.utils.ADD_KEY
import com.fappslab.imselling.utils.EDIT_KEY
import com.fappslab.imselling.utils.PRODUCT_KEY
import com.fappslab.imselling.utils.extensions.safelyNavigate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductsFragment
@Inject constructor(
    private val productsAdapter: ProductsAdapter
) : Fragment(R.layout.fragment_products) {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProductsViewModel>()
    private var pos = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductsBinding.bind(view)
        flipper(FLIPPER_POSITION_LOADING)
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
        productsAdapter.setOnItemClickListener { view, product, position ->
            pos = position
            when (view.id) {
                R.id.button_delete -> {
                    deleteDialog(product)
                }
                else -> findNavController().safelyNavigate(
                    ProductsFragmentDirections
                        .actionProductsFragmentToSaveProductFragment(product)
                )
            }
        }

        with(binding) {
            swipeProducts.setOnRefreshListener {
                getProducts()
            }

            fabAdd.setOnClickListener {
                findNavController().safelyNavigate(
                    ProductsFragmentDirections
                        .actionProductsFragmentToSaveProductFragment(null)
                )
            }
        }
    }

    private fun initObserveEvents() {
        viewModel.viewStateEvent.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is ProductsViewModel.ViewState.ShowProducts -> {
                    val list = viewState.products.toMutableList()
                    productsAdapter.submitList(list)
                    flipper(FLIPPER_POSITION_SUCCESS)
                }
                is ProductsViewModel.ViewState.ShowError -> {
                    binding.textError.text = getString(viewState.messageResId)
                    flipper(FLIPPER_POSITION_ERROR)
                }
            }
            binding.swipeProducts.isRefreshing = false
        }

        viewModel.deleteProductEvent.observe(viewLifecycleOwner) { product ->
            val oldList = productsAdapter.currentList
            val newList = oldList.toMutableList().apply {
                remove(product)
            }
            productsAdapter.submitList(newList)

            binding.progressContainer.isVisible = false

            flipper(FLIPPER_POSITION_SUCCESS)
        }

        viewModel.addButtonVisibilityEvent.observe(viewLifecycleOwner) { isAdmin ->
            if (!isAdmin) productsAdapter.setOnItemClickListener(null)
            binding.fabAdd.isVisible = isAdmin
        }
    }

    private fun initObserverNavBackStack() {
        /*findNavController().run {
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
        }*/

        setFragmentResultListener(PRODUCT_KEY) { _, bundle ->
            when {
                bundle.containsKey(EDIT_KEY) -> {
                    bundle.getParcelable<Product>(EDIT_KEY)?.let { product ->
                        productsAdapter.modifyItemList(pos, product)
                    }
                }
                bundle.containsKey(ADD_KEY) -> {
                    bundle.getParcelable<Product>(ADD_KEY)?.let { product ->
                        val oldList = productsAdapter.currentList
                        val newList = oldList.toMutableList().apply {
                            add(product)
                        }
                        productsAdapter.submitList(newList)
                        binding.recyclerProducts.smoothScrollToPosition(newList.size - 1)

                        flipper(FLIPPER_POSITION_SUCCESS)
                    }
                }
            }
        }
    }

    private fun getProducts() {
        viewModel.getProducts()
    }

    private fun deleteDialog(product: Product) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_title))
            .setNegativeButton(getString(R.string.delete_confirm)) { dialog, _ ->
                binding.progressContainer.isVisible = true
                viewModel.deleteProduct(product)
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.delete_cancel), null)
            .show()
    }

    private fun flipper(child: Int) {
        val position = when (child) {
            FLIPPER_POSITION_SUCCESS -> {
                when (productsAdapter.itemCount) {
                    0 -> FLIPPER_POSITION_EMPTY
                    else -> FLIPPER_POSITION_SUCCESS
                }
            }
            else -> child
        }
        view?.postDelayed({
            binding.flipperContainer.displayedChild = position
        }, 500)
    }

    companion object {
        private const val FLIPPER_POSITION_LOADING = 0
        private const val FLIPPER_POSITION_ERROR = 1
        private const val FLIPPER_POSITION_EMPTY = 2
        private const val FLIPPER_POSITION_SUCCESS = 3
    }
}
