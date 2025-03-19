package com.example.final_242.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.final_242.R
import com.example.final_242.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupProfileOptions()

        return root
    }

    private fun setupProfileOptions() {
        // My Orders option
        binding.myOrdersOption.setOnClickListener {
            // Navigate to orders screen
            findNavController().navigate(R.id.action_navigation_profile_to_ordersFragment)
        }

        // Shipping Addresses option
        binding.shippingAddressesOption.setOnClickListener {
            Toast.makeText(context, "Shipping Addresses", Toast.LENGTH_SHORT).show()
            // In a real app, you would navigate to a shipping addresses screen
        }

        // Payment Methods option
        binding.paymentMethodsOption.setOnClickListener {
            Toast.makeText(context, "Payment Methods", Toast.LENGTH_SHORT).show()
            // In a real app, you would navigate to a payment methods screen
        }

        // Settings option
        binding.settingsOption.setOnClickListener {
            Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show()
            // In a real app, you would navigate to a settings screen
        }

        // Logout button
        binding.logoutButton.setOnClickListener {
            Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
            // In a real app, you would implement logout functionality
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
