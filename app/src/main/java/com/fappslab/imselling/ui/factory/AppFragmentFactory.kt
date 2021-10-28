package com.fappslab.imselling.ui.factory

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.fappslab.imselling.ui.products.ProductsAdapter
import com.fappslab.imselling.ui.products.ProductsFragment
import com.fappslab.imselling.ui.saveproduct.SaveProductFragment
import javax.inject.Inject

class AppFragmentFactory
@Inject constructor(
    private val productsAdapter: ProductsAdapter,
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            ProductsFragment::class.java.name -> ProductsFragment(productsAdapter)
            SaveProductFragment::class.java.name -> SaveProductFragment()
            else -> return super.instantiate(classLoader, className)
        }
    }
}
