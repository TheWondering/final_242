package com.example.final_242.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.final_242.R
import com.example.final_242.adapter.ProductAdapter
import com.example.final_242.databinding.FragmentHomeBinding
import com.example.final_242.model.Product

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductAdapter
    private lateinit var homeViewModel: HomeViewModel

    private val categoryViews = mutableListOf<TextView>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupCategoryFilters()
        setupRecyclerView()
        observeViewModel()

        return root
    }

    private fun setupCategoryFilters() {
        categoryViews.apply {
            add(binding.categoryAll)
            add(binding.categoryWoman)
            add(binding.categoryMan)
            add(binding.categoryBoy)
            add(binding.categoryGirl)
        }

        categoryViews.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                updateCategorySelection(index)
                homeViewModel.filterProductsByCategory(
                    when(index) {
                        0 -> ""  // All
                        1 -> "woman"
                        2 -> "man"
                        3 -> "boy"
                        4 -> "girl"
                        else -> ""
                    }
                )
            }
        }
    }

    private fun updateCategorySelection(selectedIndex: Int) {
        categoryViews.forEachIndexed { index, textView ->
            if (index == selectedIndex) {
                textView.setBackgroundResource(R.drawable.category_selected_background)
                textView.setTextColor(resources.getColor(android.R.color.white, null))
            } else {
                textView.setBackgroundResource(R.drawable.category_unselected_background)
                textView.setTextColor(resources.getColor(android.R.color.darker_gray, null))
            }
        }
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(
            emptyList(),
            onFavoriteClick = { product ->
                // Handle favorite click
                Toast.makeText(context, "${product.name} ${if (product.isFavorite) "added to" else "removed from"} favorites", Toast.LENGTH_SHORT).show()
            },
            onAddToCartClick = { product ->
                // Handle add to cart click
                Toast.makeText(context, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
            }
        )

        binding.productsRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = productAdapter
        }
    }

    private fun observeViewModel() {
        homeViewModel.products.observe(viewLifecycleOwner) { products ->
            productAdapter.updateProducts(products)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}