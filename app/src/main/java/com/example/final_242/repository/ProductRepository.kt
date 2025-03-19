package com.example.final_242.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.final_242.model.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductRepository {
    private val database = FirebaseDatabase.getInstance("https://final242project-405c0-default-rtdb.europe-west1.firebasedatabase.app")
    private val productsRef = database.getReference("products")

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    init {
        fetchAllProducts()
    }

    fun fetchAllProducts() {
        _loading.value = true

        productsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productsList = mutableListOf<Product>()

                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    product?.let {
                        it.id = productSnapshot.key ?: ""
                        productsList.add(it)
                    }
                }

                _products.value = productsList
                _loading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                _loading.value = false
            }
        })
    }

    fun getProductsByCategory(category: String): LiveData<List<Product>> {
        val filteredProducts = MutableLiveData<List<Product>>()

        productsRef.orderByChild("category").equalTo(category)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val productsList = mutableListOf<Product>()

                    for (productSnapshot in snapshot.children) {
                        val product = productSnapshot.getValue(Product::class.java)
                        product?.let {
                            it.id = productSnapshot.key ?: ""
                            productsList.add(it)
                        }
                    }

                    filteredProducts.value = productsList
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })

        return filteredProducts
    }

    fun searchProducts(query: String): LiveData<List<Product>> {
        val searchResults = MutableLiveData<List<Product>>()

        productsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productsList = mutableListOf<Product>()

                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    product?.let {
                        it.id = productSnapshot.key ?: ""
                        if (it.name.contains(query, ignoreCase = true) ||
                            it.description.contains(query, ignoreCase = true)) {
                            productsList.add(it)
                        }
                    }
                }

                searchResults.value = productsList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        return searchResults
    }

    fun getProductById(productId: String): LiveData<Product?> {
        val productData = MutableLiveData<Product?>()

        productsRef.child(productId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val product = snapshot.getValue(Product::class.java)
                product?.let {
                    it.id = snapshot.key ?: ""
                }
                productData.value = product
            }

            override fun onCancelled(error: DatabaseError) {
                productData.value = null
            }
        })

        return productData
    }

    companion object {
        @Volatile
        private var instance: ProductRepository? = null

        fun getInstance(): ProductRepository {
            return instance ?: synchronized(this) {
                instance ?: ProductRepository().also { instance = it }
            }
        }
    }
}
