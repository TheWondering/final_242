package com.example.final_242.ui.productdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.final_242.model.Product
import com.example.final_242.repository.ProductRepository

class ProductDetailsViewModel : ViewModel() {

    private val productRepository = ProductRepository.getInstance()

    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product> = _product

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun loadProduct(productId: String) {
        _loading.value = true

        val productLiveData = productRepository.getProductById(productId)
        productLiveData.observeForever { product ->
            if (product != null) {
                _product.value = product
            }
            _loading.value = false
        }
    }
}