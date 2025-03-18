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
        val productId = arguments?.getInt("productId") ?: 1
        Log.d("ProductDetailsFragment", "Received product ID: $productId")

        // Load the product
        viewModel.loadProduct(productId)

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
                CartRepository.addToCart(product, 1, selectedSize)

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
                binding.productPrice.text = "${it.price}"
                binding.productDescription.text = "Classic style. Modern edge. Crafted for comfort, these ${it.name.toLowerCase()} redefine everyday wear with a perfect fit and timeless appeal. Be casual. Be confident."
                binding.productImage.setImageResource(it.imageResId)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}