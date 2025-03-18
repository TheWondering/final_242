package com.example.final_242.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.final_242.model.CartItem
import com.example.final_242.model.Product
import com.example.final_242.repository.CartRepository

class CartViewModel : ViewModel() {

    val cartItems: LiveData<List<CartItem>> = CartRepository.cartItemsLiveData
    val totalPrice: LiveData<Double> = CartRepository.totalPrice

    fun updateQuantity(cartItem: CartItem, newQuantity: Int) {
        CartRepository.updateQuantity(cartItem, newQuantity)
    }

    fun removeItem(cartItem: CartItem) {
        CartRepository.removeItem(cartItem)
    }

    fun checkout() {
        // In a real app, this would handle the checkout process
        // For now, we'll just clear the cart
        CartRepository.clearCart()
    }

    fun isCartEmpty(): Boolean {
        return CartRepository.isCartEmpty()
    }
}