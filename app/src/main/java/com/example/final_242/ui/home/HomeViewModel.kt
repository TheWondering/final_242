package com.example.final_242.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.final_242.R
import com.example.final_242.model.Product

class HomeViewModel : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val allProducts = listOf(
        Product(1, "Slim Fit Chino Trousers", 59.99, R.drawable.placeholder_image, "man"),
        Product(2, "Denim Jacket", 79.99, R.drawable.placeholder_image, "woman"),
        Product(3, "Cotton T-Shirt", 24.99, R.drawable.placeholder_image, "man"),
        Product(4, "Floral Dress", 49.99, R.drawable.placeholder_image, "woman"),
        Product(5, "Leather Boots", 89.99, R.drawable.placeholder_image, "man"),
        Product(6, "Wool Sweater", 64.99, R.drawable.placeholder_image, "woman"),
        Product(7, "Casual Shorts", 34.99, R.drawable.placeholder_image, "man"),
        Product(8, "Summer Hat", 19.99, R.drawable.placeholder_image, "woman")
    )

    private var currentCategory: String = ""

    init {
        loadProducts()
    }

    private fun loadProducts() {
        _products.value = allProducts
    }

    fun filterProductsByCategory(category: String) {
        currentCategory = category

        _products.value = if (category.isEmpty()) {
            allProducts
        } else {
            allProducts.filter { it.category == category }
        }
    }
}
