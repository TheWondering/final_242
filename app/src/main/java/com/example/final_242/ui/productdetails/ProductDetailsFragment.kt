package com.example.final_242.ui.productdetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.final_242.R
import com.example.final_242.databinding.FragmentProductDetailsBinding
import com.example.final_242.repository.CartRepository

class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProductDetailsViewModel
    private val sizeViews = mutableListOf<TextView>()
    private var selectedSizeIndex = 2 // Default to M (index 2)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(ProductDetailsViewModel::class.java)

        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Get product ID from arguments
        val productId = arguments?.getString("productId") ?: ""
        Log.d("ProductDetailsFragment", "Received product ID: $productId")

        if (productId.isNotEmpty()) {
            // Load the product
            viewModel.loadProduct(productId)
        } else {
            Toast.makeText(context, "Product not found", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        setupViews()
        observeViewModel()

        return root
    }

    private fun setupViews() {
        // Back button
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // Size selection
        sizeViews.apply {
            add(binding.sizeXs)
            add(binding.sizeS)
            add(binding.sizeM)
            add(binding.sizeL)
            add(binding.sizeXl)
            add(binding.sizeXxl)
        }

        sizeViews.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                updateSizeSelection(index)
            }
        }

        // Add to cart button
        binding.addToCartButton.setOnClickListener {
            val sizes = arrayOf("XS", "S", "M", "L", "XL", "XXL")
            val selectedSize = sizes[selectedSizeIndex]

            viewModel.product.value?.let { product ->
                // Add the product to the cart
                CartRepository.getInstance().addToCart(product, 1, selectedSize)

                Toast.makeText(
                    context,
                    "${product.name} (Size: $selectedSize) added to cart",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateSizeSelection(selectedIndex: Int) {
        selectedSizeIndex = selectedIndex

        sizeViews.forEachIndexed { index, textView ->
            if (index == selectedIndex) {
                textView.setBackgroundResource(R.drawable.size_selected_background)
                textView.setTextColor(resources.getColor(android.R.color.white, null))
            } else {
                textView.setBackgroundResource(R.drawable.size_unselected_background)
                textView.setTextColor(resources.getColor(android.R.color.black, null))
            }
        }
    }

    private fun observeViewModel() {
        viewModel.product.observe(viewLifecycleOwner) { product ->
            product?.let {
                binding.productName.text = it.name
                binding.productPrice.text = it.getFormattedPrice()
                binding.productDescription.text = it.description

                // Load image with Glide
                Glide.with(this)
                    .load(it.imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(binding.productImage)

                // Update available sizes based on product data
                updateAvailableSizes(it.getSizesList())
            }
        }
    }

    private fun updateAvailableSizes(availableSizes: List<String>) {
        val standardSizes = listOf("XS", "S", "M", "L", "XL", "XXL")

        sizeViews.forEachIndexed { index, textView ->
            val size = standardSizes.getOrNull(index) ?: ""
            val isAvailable = availableSizes.contains(size)

            textView.isEnabled = isAvailable
            textView.alpha = if (isAvailable) 1.0f else 0.5f

            // If the currently selected size is not available, select the first available size
            if (index == selectedSizeIndex && !isAvailable) {
                val firstAvailableIndex = standardSizes.indexOfFirst { availableSizes.contains(it) }
                if (firstAvailableIndex >= 0) {
                    updateSizeSelection(firstAvailableIndex)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}