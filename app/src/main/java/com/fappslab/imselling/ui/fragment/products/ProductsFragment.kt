package com.fappslab.imselling.ui.fragment.products

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fappslab.imselling.R
import com.fappslab.imselling.databinding.FragmentProductsBinding
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
        initObserveEvents()
        viewModel.getProducts()
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

    private fun initObserveEvents() {
        viewModel.getProductsEvent.observe(viewLifecycleOwner) { products ->
            productsAdapter.submitList(products)
        }
    }
}
