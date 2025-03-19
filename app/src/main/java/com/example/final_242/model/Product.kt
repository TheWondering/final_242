package com.example.final_242.model

data class Product(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var category: String = "",
    var imageUrl: String = "",
    var sizes: List<String> = listOf(),  // Changed from Map to List
    var isFavorite: Boolean = false
) {
    // Empty constructor required for Firebase
    constructor() : this("", "", "", 0.0, "", "", listOf())  // Changed from mapOf() to listOf()

    // Add this method to format the price
    fun getFormattedPrice(): String {
        return "$price"
    }

    // Update this method to return the sizes directly since it's already a list
    fun getSizesList(): List<String> {
        return sizes
    }
}