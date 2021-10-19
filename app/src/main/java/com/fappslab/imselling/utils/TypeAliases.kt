package com.fappslab.imselling.utils

import android.view.View
import com.fappslab.imselling.domain.model.Product

typealias ItemClicked = (view: View, product: Product, position: Int) -> Unit
