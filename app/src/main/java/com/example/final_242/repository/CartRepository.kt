package com.example.final_242.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.final_242.model.CartItem
import com.example.final_242.model.Product

// Singleton pattern for cart repository
object CartRepository {

    private val cartItems = mutableListOf<CartItem>()
    private val _cartItemsLiveData = MutableLiveData<List<CartItem>>()
    val cartItemsLiveData: LiveData<List<CartItem>> = _cartItemsLiveData

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double> = _totalPrice

    init {
        updateLiveData()
    }

    fun addToCart(product: Product, quantity: Int = 1, size: String = "M", color: String = "Default") {
        // Check if product already exists in cart
        val existingItem = cartItems.find { it.product.id == product.id && it.size == size && it.color == color }

        if (existingItem != null) {
            // Update quantity if item already exists
            existingItem.quantity += quantity
        } else {
            // Add new item if it doesn't exist
            cartItems.add(CartItem(product, quantity, size, color))
        }

        updateLiveData()
    }

    fun updateQuantity(cartItem: CartItem, newQuantity: Int) {
        val index = cartItems.indexOf(cartItem)
        if (index != -1) {
            cartItems[index].quantity = newQuantity
            updateLiveData()
        }
    }

    fun removeItem(cartItem: CartItem) {
        cartItems.remove(cartItem)
        updateLiveData()
    }

    fun clearCart() {
        cartItems.clear()
        updateLiveData()
    }

    fun getCartItems(): List<CartItem> {
        return cartItems.toList()
    }

    fun isCartEmpty(): Boolean {
        return cartItems.isEmpty()
    }

    private fun updateLiveData() {
        _cartItemsLiveData.value = cartItems.toList()
        _totalPrice.value = cartItems.sumOf { it.totalPrice }
    }
}
