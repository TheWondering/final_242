package com.example.final_242.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.final_242.model.Product
import com.example.final_242.repository.ProductRepository

class HomeViewModel : ViewModel() {

    private val _products = MediatorLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    // Use a Set to store selected categories
    private val selectedCategories = mutableSetOf<String>()
    private var currentSearchQuery: String = ""
    private var currentMinPrice: Double = 0.0
    private var currentMaxPrice: Double = Double.MAX_VALUE

    // Use the ProductRepository instead of hardcoded products
    private val productRepository = ProductRepository.getInstance()
    private var allProducts = listOf<Product>()

    init {
        _loading.value = true

        // Add the repository's products as a source to the MediatorLiveData
        _products.addSource(productRepository.products) { products ->
            allProducts = products
            applyAllFilters()
            _loading.value = false
        }

        // Also observe the loading state from the repository
        _loading.value = productRepository.loading.value ?: true
    }

    fun getCurrentCategory(): String {
        return if (selectedCategories.size == 1) selectedCategories.first() else ""
    }

    // Add this method to get all selected categories
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

    // Method to apply filters with a Set of categories
    fun applyFilters(categories: Set<String>, minPrice: Double, maxPrice: Double) {
        selectedCategories.clear()
        selectedCategories.addAll(categories)
        currentMinPrice = minPrice
        currentMaxPrice = maxPrice
        applyAllFilters()
    }

    // Overloaded method for just price filters
    fun applyFilters(minPrice: Double, maxPrice: Double) {
        currentMinPrice = minPrice
        currentMaxPrice = maxPrice
        applyAllFilters()
    }

    private fun applyAllFilters() {
        var filteredProducts = allProducts

        // Apply category filter if set
        if (selectedCategories.isNotEmpty()) {
            filteredProducts = filteredProducts.filter {
                selectedCategories.contains(it.category.toLowerCase())
            }
        }

        // Apply search filter if set
        if (currentSearchQuery.isNotEmpty()) {
            filteredProducts = filteredProducts.filter {
                it.name.toLowerCase().contains(currentSearchQuery) ||
                it.description?.toLowerCase()?.contains(currentSearchQuery) == true
            }
        }

        // Apply price filter
        filteredProducts = filteredProducts.filter {
            it.price >= currentMinPrice && it.price <= currentMaxPrice
        }

        _products.value = filteredProducts
    }

    fun clearSearch() {
        currentSearchQuery = ""
        applyAllFilters()
    }

    // Add a method to refresh products if needed
    fun refreshProducts() {
        _loading.value = true
        productRepository.fetchAllProducts()
    }
}