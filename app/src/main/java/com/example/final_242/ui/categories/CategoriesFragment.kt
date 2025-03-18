package com.example.final_242.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.final_242.R
import com.example.final_242.adapter.CategoryAdapter
import com.example.final_242.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoriesViewModel: CategoriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        categoriesViewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)

        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        setupBackButton()

        return root
    }

    private fun setupRecyclerView() {
        val categoryAdapter = CategoryAdapter(categoriesViewModel.getCategories()) { category ->
            // Navigate to Home fragment with category filter
            val bundle = Bundle().apply {
                putString("categoryFilter", category.name.toLowerCase())
            }
            findNavController().navigate(
                R.id.action_navigation_grid_to_navigation_home,
                bundle
            )
        }

        binding.categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = categoryAdapter
        }
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
