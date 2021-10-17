package com.fappslab.imselling.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.fappslab.imselling.R
import com.fappslab.imselling.databinding.FragmentProductsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment(R.layout.fragment_products) {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductsBinding.bind(view)
    }

}
