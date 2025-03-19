package com.example.final_242.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.final_242.model.Product

object FavoritesRepository {

    private val favoriteProducts = mutableListOf<Product>()
    private val _favoritesLiveData = MutableLiveData<List<Product>>()
    val favoritesLiveData: LiveData<List<Product>> = _favoritesLiveData

    init {
        updateLiveData()
    }

    fun addToFavorites(product: Product) {
        if (!isProductInFavorites(product)) {
            product.isFavorite = true
            favoriteProducts.add(product)
            updateLiveData()
        }
    }

    fun removeFromFavorites(product: Product) {
        val productToRemove = favoriteProducts.find { it.id == product.id }
        if (productToRemove != null) {
            productToRemove.isFavorite = false
            favoriteProducts.remove(productToRemove)
            updateLiveData()
        }
    }

    fun isProductInFavorites(product: Product): Boolean {
        return favoriteProducts.any { it.id == product.id }
    }

    fun getFavorites(): List<Product> {
        return favoriteProducts.toList()
    }

    fun toggleFavorite(product: Product) {
        if (isProductInFavorites(product)) {
            removeFromFavorites(product)
        } else {
            addToFavorites(product)
        }
    }

    private fun updateLiveData() {
        _favoritesLiveData.value = favoriteProducts.toList()
    }

    // Singleton pattern
    fun getInstance(): FavoritesRepository {
        return this
    }
}
