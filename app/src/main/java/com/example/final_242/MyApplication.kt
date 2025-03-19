package com.example.final_242

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Enable Firebase offline capabilities
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}
