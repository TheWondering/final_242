package com.example.final_242.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.final_242.R
import com.example.final_242.model.Order
import com.example.final_242.model.OrderStatus
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Locale

class OrdersAdapter(
    private var orders: List<Order>,
    private val onOrderClick: (Order) -> Unit
) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val statusLabel: TextView = view.findViewById(R.id.status_label)
        val orderId: TextView = view.findViewById(R.id.order_id)
        val orderPrice: TextView = view.findViewById(R.id.order_price)
        val orderDate: TextView = view.findViewById(R.id.order_date)
        val arrowIcon: ImageView = view.findViewById(R.id.arrow_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]

        // Set order ID
        holder.orderId.text = "#${order.id}"

        // Format price
        val format = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("USD")
        holder.orderPrice.text = format.format(order.totalPrice)

        // Format date
        val dateFormat = SimpleDateFormat("dd MMM yyyy - hh:mm a", Locale.getDefault())
        holder.orderDate.text = dateFormat.format(order.date)

        // Set status label
        holder.statusLabel.text = order.status.name.capitalize()

        // Set status label background color based on status
        val labelBackground = when (order.status) {
            OrderStatus.PENDING -> R.drawable.orange_rounded_label
            OrderStatus.PROCESSING -> R.drawable.blue_rounded_label
            OrderStatus.SHIPPED -> R.drawable.yellow_rounded_label
            OrderStatus.DELIVERED -> R.drawable.green_rounded_label
            OrderStatus.CANCELLED -> R.drawable.red_rounded_label
        }
        holder.statusLabel.setBackgroundResource(labelBackground)

        // Set click listener
        holder.itemView.setOnClickListener {
            onOrderClick(order)
        }
    }

    override fun getItemCount() = orders.size

    fun updateOrders(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}
