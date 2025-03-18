package com.example.final_242.ui.checkout

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.final_242.R
import com.example.final_242.databinding.FragmentCheckoutBinding
import com.example.final_242.repository.CartRepository

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CheckoutViewModel
    private var selectedPaymentMethod = "mastercard" // Default selected payment method

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(CheckoutViewModel::class.java)

        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupViews()
        setupPaymentMethodSelection()
        setupCardInputListeners()
        observeViewModel()

        return root
    }

    private fun setupViews() {
        // Back button
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // Edit address button
        binding.editAddressButton.setOnClickListener {
            Toast.makeText(context, "Edit address functionality", Toast.LENGTH_SHORT).show()
        }

        // Place order button
        binding.placeOrderButton.setOnClickListener {
            if (validateInputs()) {
                viewModel.placeOrder()
                findNavController().navigate(R.id.action_checkoutFragment_to_orderSuccessFragment)
            }
        }
    }

    private fun setupPaymentMethodSelection() {
        // Set initial selection
        updatePaymentMethodSelection(selectedPaymentMethod)

        // Setup click listeners
        binding.visaCard.setOnClickListener {
            selectedPaymentMethod = "visa"
            updatePaymentMethodSelection(selectedPaymentMethod)
        }

        binding.mastercardCard.setOnClickListener {
            selectedPaymentMethod = "mastercard"
            updatePaymentMethodSelection(selectedPaymentMethod)
        }

        binding.otherCard.setOnClickListener {
            selectedPaymentMethod = "other"
            updatePaymentMethodSelection(selectedPaymentMethod)
        }
    }

    private fun updatePaymentMethodSelection(method: String) {
        binding.visaCheck.visibility = if (method == "visa") View.VISIBLE else View.GONE
        binding.mastercardCheck.visibility = if (method == "mastercard") View.VISIBLE else View.GONE
        binding.otherCheck.visibility = if (method == "other") View.VISIBLE else View.GONE
    }

    private fun setupCardInputListeners() {
        // Card number input
        binding.cardNumberInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Format card number with spaces
                if (!s.isNullOrEmpty() && s.length % 5 == 4 && s.length < 19 && s[s.length - 1] != ' ') {
                    val formatted = StringBuilder(s).insert(s.length - 1, " ").toString()
                    binding.cardNumberInput.setText(formatted)
                    binding.cardNumberInput.setSelection(formatted.length)
                }

                // Update card preview
                binding.cardNumberPreview.text = if (s.isNullOrEmpty()) "5698 56254 6786 9979" else s
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Card holder input
        binding.cardHolderInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Update card preview
                binding.cardHolderPreview.text = if (s.isNullOrEmpty()) "John Doe" else s
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Expiry date input
        binding.expiryDateInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Format expiry date with slash
                if (!s.isNullOrEmpty() && s.length == 2 && before == 0) {
                    val formatted = "$s/"
                    binding.expiryDateInput.setText(formatted)
                    binding.expiryDateInput.setSelection(formatted.length)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun observeViewModel() {
        viewModel.orderSummary.observe(viewLifecycleOwner) { summary ->
            binding.subtotalText.text = "$${summary.subtotal}"
            binding.shippingText.text = "$${summary.shipping}"
            binding.discountText.text = "-$${summary.discount}"
            binding.totalText.text = "$${summary.total}"
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Validate card number
        val cardNumber = binding.cardNumberInput.text.toString().replace(" ", "")
        if (cardNumber.isEmpty() || cardNumber.length < 16) {
            binding.cardNumberInput.error = "Please enter a valid card number"
            isValid = false
        }

        // Validate card holder
        val cardHolder = binding.cardHolderInput.text.toString()
        if (cardHolder.isEmpty()) {
            binding.cardHolderInput.error = "Please enter card holder name"
            isValid = false
        }

        // Validate expiry date
        val expiryDate = binding.expiryDateInput.text.toString()
        if (expiryDate.isEmpty() || expiryDate.length < 5 || !expiryDate.contains("/")) {
            binding.expiryDateInput.error = "Please enter a valid expiry date (MM/YY)"
            isValid = false
        }

        // Validate CVV
        val cvv = binding.cvvInput.text.toString()
        if (cvv.isEmpty() || cvv.length < 3) {
            binding.cvvInput.error = "Please enter a valid CVV"
            isValid = false
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
