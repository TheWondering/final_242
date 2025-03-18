package com.example.final_242.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.final_242.R
import com.example.final_242.model.CartItem
import java.text.NumberFormat
import java.util.Currency

class CartAdapter(
    private var cartItems: List<CartItem>,
    private val onQuantityChanged: (CartItem, Int) -> Unit,
    private val onItemRemoved: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage: ImageView = view.findViewById(R.id.product_image)
        val productName: TextView = view.findViewById(R.id.product_name)
        val productDetails: TextView = view.findViewById(R.id.product_details)
        val quantityText: TextView = view.findViewById(R.id.quantity_text)
        val decreaseButton: ImageButton = view.findViewById(R.id.decrease_button)
        val increaseButton: ImageButton = view.findViewById(R.id.increase_button)
        val productPrice: TextView = view.findViewById(R.id.product_price)
        val deleteButton: ImageButton = view.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        val product = cartItem.product

        // Set product image and details
        holder.productImage.setImageResource(product.imageResId)
        holder.productName.text = product.name
        holder.productDetails.text = "Color: ${cartItem.color}, Size: ${cartItem.size}"

        // Set quantity
        holder.quantityText.text = cartItem.quantity.toString()

        // Set price
        val format = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("USD")
        holder.productPrice.text = format.format(cartItem.totalPrice)

        // Setup quantity buttons
        holder.decreaseButton.isEnabled = cartItem.quantity > 1
        holder.decreaseButton.alpha = if (cartItem.quantity > 1) 1.0f else 0.5f

        holder.decreaseButton.setOnClickListener {
            if (cartItem.quantity > 1) {
                onQuantityChanged(cartItem, cartItem.quantity - 1)
            }
        }

        holder.increaseButton.setOnClickListener {
            onQuantityChanged(cartItem, cartItem.quantity + 1)
        }

        // Setup delete button
        holder.deleteButton.setOnClickListener {
            onItemRemoved(cartItem)
        }
    }

    override fun getItemCount() = cartItems.size

    fun updateCartItems(newCartItems: List<CartItem>) {
        cartItems = newCartItems
        notifyDataSetChanged()
    }
}
