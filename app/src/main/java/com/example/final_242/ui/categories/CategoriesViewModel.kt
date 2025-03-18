package com.example.final_242.ui.categories

import androidx.lifecycle.ViewModel
import com.example.final_242.R
import com.example.final_242.model.Category

class CategoriesViewModel : ViewModel() {

    fun getCategories(): List<Category> {
        return listOf(
            Category(1, "Man", R.drawable.placeholder_image),
            Category(2, "Woman", R.drawable.placeholder_image),
            Category(3, "Boy", R.drawable.placeholder_image),
            Category(4, "Girl", R.drawable.placeholder_image)
        )
    }
}
