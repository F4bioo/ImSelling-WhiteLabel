package com.fappslab.imselling.ui.addproduct

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fappslab.imselling.R
import com.fappslab.imselling.databinding.FragmentAddProductBinding
import com.fappslab.imselling.utils.CurrencyTextWatcher
import com.fappslab.imselling.utils.PRODUCT_KEY
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AddProductViewModel>()

    private var imageUri: Uri? = null

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            imageUri = uri
            binding.imageProduct.setImageURI(uri)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserveEvents()
        initListeners()
    }

    private fun initObserveEvents() {
        viewModel.imageUriErrorResIdEvent.observe(viewLifecycleOwner) { drawableResId ->
            if (drawableResId == R.drawable.background_product_image_error) {
                progressRules(false)
            }
            binding.imageProduct.setBackgroundResource(drawableResId)
        }

        viewModel.inputDescriptionErrorResIdEvent.observe(viewLifecycleOwner) { stringResId ->
            if (stringResId == R.string.add_product_field_error) {
                progressRules(false)
            }

            binding.inputLayoutDescription.setError(stringResId)
        }

        viewModel.inputPriceErrorResIdEvent.observe(viewLifecycleOwner) { stringResId ->
            if (stringResId == R.string.add_product_field_error) {
                progressRules(false)
            }

            binding.inputLayoutPrice.setError(stringResId)
        }

        viewModel.createProductEvent.observe(viewLifecycleOwner) { product ->
            findNavController().run {
                previousBackStackEntry?.savedStateHandle?.set(PRODUCT_KEY, product)
                popBackStack()
            }
        }
    }

    private fun initListeners() {
        binding.imageProduct.setOnClickListener {
            chooseImage()
        }

        binding.buttonAdd.setOnClickListener {
            progressRules(true)

            val description = binding.inputDescription.text.toString()
            val price = binding.inputPrice.text.toString()

            viewModel.createProduct(description, price, imageUri)
        }

        binding.inputPrice.run {
            addTextChangedListener(CurrencyTextWatcher(this))
        }
    }

    private fun chooseImage() {
        getContent.launch("image/*")
    }

    private fun TextInputLayout.setError(stringResId: Int?) {
        error = if (stringResId != null) {
            getString(stringResId)
        } else null
    }

    private fun progressRules(show: Boolean) {
        binding.buttonAdd.isEnabled = !show
        binding.progressContainer.isVisible = show
    }
}
