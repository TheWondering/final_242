package com.example.final_242

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.final_242.databinding.ActivityMainBinding
import com.example.final_242.repository.CartRepository

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_grid, R.id.navigation_favorites,
                R.id.navigation_cart, R.id.navigation_profile
            )
        )

        // Hide the ActionBar
        supportActionBar?.hide()

        navView.setupWithNavController(navController)

        // Observe cart items to update badge
        CartRepository.cartItemsLiveData.observe(this) { cartItems ->
            val badge = navView.getOrCreateBadge(R.id.navigation_cart)
            if (cartItems.isEmpty()) {
                badge.isVisible = false
            } else {
                badge.isVisible = true
                badge.number = cartItems.sumOf { it.quantity }
            }
        }
    }}