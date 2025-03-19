package com.example.final_242.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.final_242.model.CartItem
import com.example.final_242.model.Order
import com.example.final_242.model.OrderStatus
import com.example.final_242.model.Product
import com.example.final_242.R
import java.util.Date
import java.util.UUID
import java.util.concurrent.TimeUnit

class OrdersViewModel : ViewModel() {

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> = _orders
    // Sample products for demonstration
    private val sampleProducts = listOf(
        Product(id = "1", name = "Slim Fit Chino Trousers", price = 59.99, imageUrl = "https://example.com/image1.jpg", category = "man"),
        Product(id = "2", name = "Denim Jacket", price = 79.99, imageUrl = "https://example.com/image2.jpg", category = "woman"),
        Product(id = "3", name = "Cotton T-Shirt", price = 24.99, imageUrl = "https://example.com/image3.jpg", category = "man"),
        Product(id = "4", name = "Floral Dress", price = 49.99, imageUrl = "https://example.com/image4.jpg", category = "woman"),
        Product(id = "5", name = "Leather Boots", price = 89.99, imageUrl = "https://example.com/image5.jpg", category = "man")
    )
    init {
        loadSampleOrders()
    }

    private fun loadSampleOrders() {
        // Create some sample orders for demonstration
        val sampleOrders = listOf(
            Order(
                id = generateOrderId(),
                totalPrice = 99.98,
                date = Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2)),
                status = OrderStatus.SHIPPED,
                items = listOf(
                    CartItem(sampleProducts[0], 1, "M", "Blue"),
                    CartItem(sampleProducts[2], 1, "L", "Black")
                )
            ),
            Order(
                id = generateOrderId(),
                totalPrice = 129.98,
                date = Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(5)),
                status = OrderStatus.DELIVERED,
                items = listOf(
                    CartItem(sampleProducts[1], 1, "S", "Blue"),
                    CartItem(sampleProducts[3], 1, "M", "Floral")
                )
            ),
            Order(
                id = generateOrderId(),
                totalPrice = 89.99,
                date = Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(12)),
                status = OrderStatus.PROCESSING,
                items = listOf(
                    CartItem(sampleProducts[4], 1, "42", "Brown")
                )
            )
        )

        _orders.value = sampleOrders
    }

    private fun generateOrderId(): String {
        // Generate a random order ID
        return UUID.randomUUID().toString().substring(0, 18)
    }
}
