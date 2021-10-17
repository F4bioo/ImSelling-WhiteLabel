package com.fappslab.imselling.ui.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.fappslab.imselling.R
import com.fappslab.imselling.databinding.ItemProductBinding
import com.fappslab.imselling.domain.model.Product
import com.fappslab.imselling.utils.toCurrency

class ProductsAdapter : ListAdapter<Product, ProductsAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewBinding(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun viewBinding(product: Product) {
            binding.run {
                imageProduct.load(product.imageUrl) {
                    placeholder(R.drawable.ic_placeholder)
                    crossfade(true)
                    crossfade(500)
                    transformations(RoundedCornersTransformation(16f))
                }
                textDescription.text = product.description
                textPrice.text = product.price.toCurrency()
            }
        }

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val binding = ItemProductBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)

                return ViewHolder(binding)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }
}
