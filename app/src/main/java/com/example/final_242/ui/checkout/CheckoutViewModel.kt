package com.example.final_242.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.final_242.repository.CartRepository

data class OrderSummary(
    val subtotal: Double,
    val shipping: Double,
    val discount: Double,
    val total: Double
)

class CheckoutViewModel : ViewModel() {

    private val _orderSummary = MutableLiveData<OrderSummary>()
    val orderSummary: LiveData<OrderSummary> = _orderSummary

    init {
        calculateOrderSummary()
    }

    private fun calculateOrderSummary() {
        val subtotal = CartRepository.totalPrice.value ?: 0.0
        val shipping = 24.36 // Fixed shipping cost for demo
        val discount = if (subtotal > 100) 20.0 else 0.0 // Example discount logic
        val total = subtotal + shipping - discount

        _orderSummary.value = OrderSummary(
            subtotal = Math.round(subtotal * 100) / 100.0,
            shipping = shipping,
            discount = discount,
            total = Math.round(total * 100) / 100.0
        )
    }

    fun placeOrder() {
        // In a real app, this would send the order to a server
        // For now, we'll just clear the cart
        CartRepository.clearCart()
    }
}
