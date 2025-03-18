package com.example.final_242.ui.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.final_242.R
import com.example.final_242.adapter.ProductAdapter
import com.example.final_242.databinding.FragmentHomeBinding
import com.example.final_242.model.Product
import com.example.final_242.repository.CartRepository
import com.google.android.material.slider.RangeSlider
import java.text.NumberFormat
import java.util.Currency

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductAdapter
    private lateinit var homeViewModel: HomeViewModel

    private val categoryViews = mutableListOf<TextView>()
    private var filterDialog: Dialog? = null
    private var selectedCategoryInFilter: String = ""
    private var minPrice: Float = 0f
    private var maxPrice: Float = 300f

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
        setupSearchAndFilter()
        observeViewModel()

        // Check if we have a category filter from arguments
        arguments?.getString("categoryFilter")?.let { category ->
            if (category.isNotEmpty()) {
                // Apply the filter
                homeViewModel.filterProductsByCategory(category)

                // Update the UI to show the selected category
                updateCategorySelectionByName(category)
            }
        }

        return root
    }

    private fun setupSearchAndFilter() {
        // Setup search text change listener to show/hide clear button
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearSearchButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Setup search functionality
        binding.searchEditText.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchQuery = textView.text.toString().trim()
                if (searchQuery.isNotEmpty()) {
                    homeViewModel.searchProducts(searchQuery)
                } else {
                    // If search is empty, reset to current category filter
                    homeViewModel.filterProductsByCategory(homeViewModel.getCurrentCategory())
                }
                return@setOnEditorActionListener true
            }
            false
        }

        // Setup clear search button
        binding.clearSearchButton.setOnClickListener {
            binding.searchEditText.text.clear()
            // Reset search query and refresh products
            homeViewModel.clearSearch()
        }

        // Setup filter button
        binding.filterButton.setOnClickListener {
            showFilterDialog()
        }
    }

    private fun showFilterDialog() {
        // Create dialog
        filterDialog = Dialog(requireContext())
        filterDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        filterDialog?.setContentView(R.layout.dialog_filter)

        // Make dialog background transparent to show rounded corners
        filterDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Setup close button
        val closeButton = filterDialog?.findViewById<ImageButton>(R.id.close_button)
        closeButton?.setOnClickListener {
            filterDialog?.dismiss()
        }

        // Setup category selection
        val categoryMen = filterDialog?.findViewById<TextView>(R.id.category_men)
        val categoryWomen = filterDialog?.findViewById<TextView>(R.id.category_women)
        val categoryBoys = filterDialog?.findViewById<TextView>(R.id.category_boys)
        val categoryGirls = filterDialog?.findViewById<TextView>(R.id.category_girls)

        val categoryViews = listOf(categoryMen, categoryWomen, categoryBoys, categoryGirls)
        val categoryValues = listOf("man", "woman", "boy", "girl")

        // Set initial selection based on current filters
        val selectedCategories = homeViewModel.getSelectedCategories().toMutableSet()

        updateFilterCategorySelections(categoryViews, categoryValues, selectedCategories)

        // Setup category click listeners
        categoryViews.forEachIndexed { index, textView ->
            textView?.setOnClickListener {
                val category = categoryValues[index]
                if (selectedCategories.contains(category)) {
                    selectedCategories.remove(category)
                } else {
                    selectedCategories.add(category)
                }
                updateFilterCategorySelections(categoryViews, categoryValues, selectedCategories)
            }
        }

        // Setup price range slider
        val priceRangeSlider = filterDialog?.findViewById<RangeSlider>(R.id.price_range_slider)
        val priceRangeText = filterDialog?.findViewById<TextView>(R.id.price_range_text)

        priceRangeSlider?.setValues(minPrice, maxPrice)
        updatePriceRangeText(priceRangeText, minPrice, maxPrice)

        priceRangeSlider?.addOnChangeListener { slider, _, _ ->
            val values = slider.values
            minPrice = values[0]
            maxPrice = values[1]
            updatePriceRangeText(priceRangeText, minPrice, maxPrice)
        }

        // Setup action buttons
        val clearButton = filterDialog?.findViewById<Button>(R.id.clear_button)
        val applyButton = filterDialog?.findViewById<Button>(R.id.apply_button)

        clearButton?.setOnClickListener {
            // Reset all filters
            selectedCategories.clear()
            minPrice = 0f
            maxPrice = 300f

            // Update UI
            updateFilterCategorySelections(categoryViews, categoryValues, selectedCategories)
            priceRangeSlider?.setValues(minPrice, maxPrice)
            updatePriceRangeText(priceRangeText, minPrice, maxPrice)
        }

        applyButton?.setOnClickListener {
            // Apply filters
            homeViewModel.applyFilters(selectedCategories, minPrice.toDouble(), maxPrice.toDouble())

            // Update category selection in main UI if needed
            if (selectedCategories.isEmpty()) {
                updateCategorySelection(0) // Select "All"
            } else if (selectedCategories.size == 1) {
                updateCategorySelectionByName(selectedCategories.first())
            } else {
                // If multiple categories selected, just select "All" in the main UI
                updateCategorySelection(0)
            }

            // Dismiss dialog
            filterDialog?.dismiss()
        }
        applyButton?.setOnClickListener {
            // Apply filters
            homeViewModel.applyFilters(setOf(selectedCategoryInFilter).filter { it.isNotEmpty() }.toSet(), minPrice.toDouble(), maxPrice.toDouble())


            // Update category selection in main UI if needed
            if (selectedCategoryInFilter.isNotEmpty()) {
                updateCategorySelectionByName(selectedCategoryInFilter)
            } else {
                updateCategorySelection(0) // Select "All"
            }

            // Dismiss dialog
            filterDialog?.dismiss()
        }

        filterDialog?.show()
    }

    private fun updateFilterCategorySelection(
        categoryViews: List<TextView?>,
        categoryValues: List<String>,
        selectedCategory: String
    ) {
        categoryViews.forEachIndexed { index, textView ->
            val isSelected = categoryValues[index] == selectedCategory
            textView?.setBackgroundResource(
                if (isSelected) R.drawable.filter_category_selected
                else R.drawable.filter_category_unselected
            )
        }
    }

    private fun updatePriceRangeText(textView: TextView?, min: Float, max: Float) {
        val format = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("USD")
        textView?.text = "${format.format(min.toDouble())} - ${format.format(max.toDouble())}"
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

    private fun updateCategorySelectionByName(categoryName: String) {
        val index = when(categoryName.toLowerCase()) {
            "woman" -> 1
            "man" -> 2
            "boy" -> 3
            "girl" -> 4
            else -> 0  // All
        }
        updateCategorySelection(index)
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(
            emptyList(),
            onFavoriteClick = { product ->
                // Handle favorite click
                Toast.makeText(context, "${product.name} ${if (product.isFavorite) "added to" else "removed from"} favorites", Toast.LENGTH_SHORT).show()
            },
            onAddToCartClick = { product ->
                // Add product to cart
                CartRepository.addToCart(product)
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

    // Add this method to the HomeFragment class
    private fun updateFilterCategorySelections(
        categoryViews: List<TextView?>,
        categoryValues: List<String>,
        selectedCategories: Set<String>
    ) {
        categoryViews.forEachIndexed { index, textView ->
            val isSelected = selectedCategories.contains(categoryValues[index])
            textView?.setBackgroundResource(
                if (isSelected) R.drawable.filter_category_selected
                else R.drawable.filter_category_unselected
            )
        }
    }
}
