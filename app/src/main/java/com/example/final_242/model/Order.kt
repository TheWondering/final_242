package com.example.final_242.model

import java.util.Date

data class Order(
    val id: String = "",
    val totalPrice: Double = 0.0,
    val date: Date = Date(),
    val status: OrderStatus = OrderStatus.PENDING,
    val items: List<CartItem> = listOf()
)

enum class OrderStatus {
    PENDING,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
