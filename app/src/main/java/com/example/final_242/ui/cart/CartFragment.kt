package com.example.final_242.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.final_242.R
import com.example.final_242.adapter.CartAdapter
import com.example.final_242.databinding.FragmentCartBinding
import java.text.NumberFormat
import java.util.Currency

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        setupButtons()
        observeViewModel()

        return root
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            emptyList(),
            onQuantityChanged = { cartItem, newQuantity ->
                cartViewModel.updateQuantity(cartItem, newQuantity)
            },
            onItemRemoved = { cartItem ->
                cartViewModel.removeItem(cartItem)
            }
        )

        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
        }
    }

    private fun setupButtons() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.checkoutButton.setOnClickListener {
            if (!cartViewModel.isCartEmpty()) {
                findNavController().navigate(R.id.action_navigation_cart_to_checkoutFragment)
            } else {
                Toast.makeText(context, "Your cart is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        cartViewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            cartAdapter.updateCartItems(cartItems)

            // Show/hide empty cart message
            if (cartItems.isEmpty()) {
                binding.emptyCartText.visibility = View.VISIBLE
                binding.cartRecyclerView.visibility = View.GONE
                binding.totalPriceContainer.visibility = View.GONE
                binding.checkoutButton.isEnabled = false
                binding.checkoutButton.alpha = 0.5f
            } else {
                binding.emptyCartText.visibility = View.GONE
                binding.cartRecyclerView.visibility = View.VISIBLE
                binding.totalPriceContainer.visibility = View.VISIBLE
                binding.checkoutButton.isEnabled = true
                binding.checkoutButton.alpha = 1.0f
            }
        }

        cartViewModel.totalPrice.observe(viewLifecycleOwner) { totalPrice ->
            val format = NumberFormat.getCurrencyInstance()
            format.currency = Currency.getInstance("USD")
            binding.totalPriceText.text = format.format(totalPrice)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}