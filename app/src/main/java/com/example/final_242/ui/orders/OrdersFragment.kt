package com.example.final_242.ui.orders

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
import com.example.final_242.adapter.OrdersAdapter
import com.example.final_242.databinding.FragmentOrdersBinding

class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    private lateinit var ordersViewModel: OrdersViewModel
    private lateinit var ordersAdapter: OrdersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ordersViewModel = ViewModelProvider(this).get(OrdersViewModel::class.java)

        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        observeViewModel()
        setupBackButton()
        setupShopNowButton()

        return root
    }

    private fun setupRecyclerView() {
        ordersAdapter = OrdersAdapter(
            emptyList(),
            onOrderClick = { order ->
                // In a real app, you would navigate to order details
                Toast.makeText(context, "Order #${order.id} clicked", Toast.LENGTH_SHORT).show()
            }
        )

        binding.ordersRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ordersAdapter
        }
    }

    private fun observeViewModel() {
        ordersViewModel.orders.observe(viewLifecycleOwner) { orders ->
            ordersAdapter.updateOrders(orders)

            // Show/hide empty state
            if (orders.isEmpty()) {
                binding.emptyOrdersContainer.visibility = View.VISIBLE
                binding.ordersRecyclerView.visibility = View.GONE
            } else {
                binding.emptyOrdersContainer.visibility = View.GONE
                binding.ordersRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupShopNowButton() {
        binding.shopNowButton.setOnClickListener {
            // Navigate to home screen to shop
            findNavController().navigate(R.id.navigation_home)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
