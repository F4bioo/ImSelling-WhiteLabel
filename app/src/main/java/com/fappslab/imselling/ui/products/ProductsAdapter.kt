package com.fappslab.imselling.ui.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fappslab.imselling.config.Config
import com.fappslab.imselling.databinding.ItemProductBinding
import com.fappslab.imselling.domain.model.Product
import com.fappslab.imselling.utils.ItemClicked
import com.fappslab.imselling.utils.extensions.load
import com.fappslab.imselling.utils.toCurrency
import javax.inject.Inject

class ProductsAdapter
@Inject constructor(
    private val config: Config
) : ListAdapter<Product, ProductsAdapter.ViewHolder>(ProductsAdapter) {

    private val products = arrayListOf<Product>()
    private var onClickListener: ItemClicked? = null

    private companion object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val biding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(biding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.viewBinding(product)
    }

    override fun getItemCount() = products.size

    override fun submitList(list: MutableList<Product>?) {
        super.submitList(list)
        list?.let {
            products.clear()
            products.addAll(it)
        }
    }

    inner class ViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun viewBinding(product: Product) {
            binding.run {
                imageProduct.load(product.imageUrl)
                textDescription.text = product.description
                textPrice.text = product.price.toCurrency()
                buttonDelete.isVisible = config.isAdmin

                buttonDelete.setOnClickListener {
                    onClickListener?.invoke(it, product, layoutPosition)
                }
            }

            if (config.isAdmin) {
                itemView.setOnClickListener {
                    onClickListener?.invoke(it, product, layoutPosition)
                }
            } else itemView.isClickable = false
        }
    }

    fun setOnItemClickListener(ItemClicked: ItemClicked?) {
        onClickListener = ItemClicked
    }

    fun modifyItemList(position: Int, product: Product) {
        products[position] = product
        notifyItemChanged(position)
    }
}
