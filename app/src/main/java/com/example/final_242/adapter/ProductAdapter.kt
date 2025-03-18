package com.example.final_242.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.final_242.R
import com.example.final_242.model.Product

class ProductAdapter(
    private var products: List<Product>,
    private val onFavoriteClick: (Product) -> Unit,
    private val onAddToCartClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage: ImageView = view.findViewById(R.id.product_image)
        val productName: TextView = view.findViewById(R.id.product_name)
        val productPrice: TextView = view.findViewById(R.id.product_price)
        val favoriteButton: ImageButton = view.findViewById(R.id.favorite_button)
        val addToCartButton: ImageButton = view.findViewById(R.id.add_to_cart_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.productImage.setImageResource(product.imageResId)
        holder.productName.text = product.name
        holder.productPrice.text = "$${product.price}"

        // Update favorite icon based on product state
        updateFavoriteIcon(holder.favoriteButton, product.isFavorite)

        holder.favoriteButton.setOnClickListener {
            product.isFavorite = !product.isFavorite
            updateFavoriteIcon(holder.favoriteButton, product.isFavorite)
            onFavoriteClick(product)
        }

        holder.addToCartButton.setOnClickListener {
            onAddToCartClick(product)
        }

        // Set click listener for the entire item to navigate to details
        holder.itemView.setOnClickListener {
            // Create a bundle with the product ID
            val bundle = bundleOf("productId" to product.id)
            // Navigate to the product details fragment with the bundle
            holder.itemView.findNavController().navigate(
                R.id.action_navigation_home_to_productDetailsFragment,
                bundle
            )
        }
    }

    private fun updateFavoriteIcon(button: ImageButton, isFavorite: Boolean) {
        if (isFavorite) {
            button.setColorFilter(button.context.getResources().getColor(android.R.color.holo_red_light, null))
        } else {
            button.setColorFilter(button.context.getResources().getColor(android.R.color.darker_gray, null))
        }
    }

    override fun getItemCount() = products.size

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
