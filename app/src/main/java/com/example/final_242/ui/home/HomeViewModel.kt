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
        Product(7, "Casual Shorts", 34.99, R.drawable.placeholder_image, "boy"),
        Product(8, "Summer Hat", 19.99, R.drawable.placeholder_image, "girl")
    )

    private var currentCategory: String = ""
    private var currentSearchQuery: String = ""
    private var currentMinPrice: Double = 0.0
    private var currentMaxPrice: Double = 300.0

    init {
        loadProducts()
    }

    private fun loadProducts() {
        _products.value = allProducts
    }


    // Add this method to the HomeViewModel class
    fun clearSearch() {
        currentSearchQuery = ""
        applyAllFilters()
    }
    private val selectedCategories = mutableSetOf<String>()

    fun getCurrentCategory(): String {
        return if (selectedCategories.size == 1) selectedCategories.first() else ""
    }

    fun getSelectedCategories(): Set<String> {
        return selectedCategories.toSet()
    }

    fun filterProductsByCategory(category: String) {
        selectedCategories.clear()
        if (category.isNotEmpty()) {
            selectedCategories.add(category)
        }
        applyAllFilters()
    }

    fun searchProducts(query: String) {
        currentSearchQuery = query.toLowerCase()
        applyAllFilters()
    }

    fun applyFilters(categories: Set<String>, minPrice: Double, maxPrice: Double) {
        selectedCategories.clear()
        selectedCategories.addAll(categories)
        currentMinPrice = minPrice
        currentMaxPrice = maxPrice
        applyAllFilters()
    }

    private fun applyAllFilters() {
        var filteredProducts = allProducts

        // Apply category filter
        if (selectedCategories.isNotEmpty()) {
            filteredProducts = filteredProducts.filter {
                selectedCategories.contains(it.category.toLowerCase())
            }
        }

        // Apply search filter
        if (currentSearchQuery.isNotEmpty()) {
            filteredProducts = filteredProducts.filter {
                it.name.toLowerCase().contains(currentSearchQuery)
            }
        }

        // Apply price filter
        filteredProducts = filteredProducts.filter {
            it.price >= currentMinPrice && it.price <= currentMaxPrice
        }

        _products.value = filteredProducts
    }
}
