package com.example.final_242.model

data class CartItem(
    val product: Product,
    var quantity: Int = 1,
    val size: String = "M",
    val color: String = "Default"
) {
    val totalPrice: Double
        get() = product.price * quantity
}
