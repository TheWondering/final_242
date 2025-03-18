package com.example.final_242.ui.ordersuccess

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.final_242.R
import com.example.final_242.databinding.FragmentOrderSuccessBinding

class OrderSuccessFragment : Fragment() {

    private var _binding: FragmentOrderSuccessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderSuccessBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Animate the checkmark with a simple scale animation
        animateCheckmark()

        // Setup continue shopping button
        binding.continueShoppingButton.setOnClickListener {
            // Navigate back to home screen
            findNavController().navigate(R.id.action_orderSuccessFragment_to_navigation_home)
        }

        return root
    }

    private fun animateCheckmark() {
        // Start with the checkmark scaled down and invisible
        binding.checkmarkImage.scaleX = 0f
        binding.checkmarkImage.scaleY = 0f
        binding.checkmarkImage.alpha = 0f

        // Create scale animations
        val scaleX = ObjectAnimator.ofFloat(binding.checkmarkImage, View.SCALE_X, 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(binding.checkmarkImage, View.SCALE_Y, 0f, 1f)
        val alpha = ObjectAnimator.ofFloat(binding.checkmarkImage, View.ALPHA, 0f, 1f)

        // Create animator set
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY, alpha)
        animatorSet.duration = 500
        animatorSet.interpolator = OvershootInterpolator()

        // Start the animation
        animatorSet.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}