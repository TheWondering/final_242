package com.example.final_242.ui.productdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.final_242.R
import com.example.final_242.model.Product

class ProductDetailsViewModel : ViewModel() {

    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product> = _product

    // Sample products (in a real app, this would come from a repository)
    private val sampleProducts = listOf(
        Product(1, "Slim Fit Chino Trousers", 59.99, R.drawable.placeholder_image, "man"),
        Product(2, "Denim Jacket", 79.99, R.drawable.placeholder_image, "woman"),
        Product(3, "Cotton T-Shirt", 24.99, R.drawable.placeholder_image, "man"),
        Product(4, "Floral Dress", 49.99, R.drawable.placeholder_image, "woman"),
        Product(5, "Leather Boots", 89.99, R.drawable.placeholder_image, "man"),
        Product(6, "Wool Sweater", 64.99, R.drawable.placeholder_image, "woman"),
        Product(7, "Casual Shorts", 34.99, R.drawable.placeholder_image, "man"),
        Product(8, "Summer Hat", 19.99, R.drawable.placeholder_image, "woman")
    )

    fun loadProduct(productId: Int) {
        // Find the product by ID or load the first one if not found
        val foundProduct = sampleProducts.find { it.id == productId }
        if (foundProduct != null) {
            _product.value = foundProduct
        } else {
            // If product not found, load the first one as a fallback
            _product.value = sampleProducts.first()
        }
    }
}
