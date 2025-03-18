package com.example.final_242.model

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val imageResId: Int,
    val category: String,
    var isFavorite: Boolean = false
)
