package com.fappslab.imselling.ui.saveproduct

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fappslab.imselling.R
import com.fappslab.imselling.databinding.FragmentSaveProductBinding
import com.fappslab.imselling.utils.*
import com.fappslab.imselling.utils.extensions.load
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveProductFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSaveProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SaveProductViewModel>()
    private val args by navArgs<SaveProductFragmentArgs>()

    private var productId: String? = null
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
        _binding = FragmentSaveProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.saveArgs?.let {
            with(binding) {
                productId = it.id
                imageUri = it.imageUrl.toUri()
                imageProduct.load(imageUri.toString())
                inputLayoutDescription.editText?.setText(it.description)
                inputLayoutPrice.editText?.setText(it.price.toCurrency())
                imageAdd.isVisible = false
            }
        }.apply { binding.inputDescription.requestFocus() }

        initObserveEvents()
        initListeners()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initObserveEvents() {
        viewModel.imageUriErrorResIdEvent.observe(viewLifecycleOwner) { drawableResId ->
            if (drawableResId == R.drawable.background_product_image_error) {
                progressRules(false)
            }
            binding.imageProduct.setBackgroundResource(drawableResId)
        }

        viewModel.inputDescriptionErrorResIdEvent.observe(viewLifecycleOwner) { stringResId ->
            if (stringResId == R.string.save_product_field_error) {
                progressRules(false)
            }

            binding.inputLayoutDescription.setError(stringResId)
        }

        viewModel.inputPriceErrorResIdEvent.observe(viewLifecycleOwner) { stringResId ->
            if (stringResId == R.string.save_product_field_error) {
                progressRules(false)
            }

            binding.inputLayoutPrice.setError(stringResId)
        }

        viewModel.saveProductEvent.observe(viewLifecycleOwner) { product ->
            /*findNavController().run {
                previousBackStackEntry?.savedStateHandle?.set(PRODUCT_KEY, product)
                popBackStack()
            }*/
            val requestKey = if (productId == null) ADD_KEY else EDIT_KEY
            setFragmentResult(PRODUCT_KEY, bundleOf(requestKey to product))
            findNavController().popBackStack()
        }
    }

    private fun initListeners() {
        binding.imageProduct.setOnClickListener {
            chooseImage()
        }

        binding.buttonSave.setOnClickListener {
            progressRules(true)

            val description = binding.inputDescription.text.toString()
            val price = binding.inputPrice.text.toString()

            viewModel.saveProduct(productId, description, price, imageUri)
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
        binding.buttonSave.isEnabled = !show
        binding.progressContainer.isVisible = show
    }
}
