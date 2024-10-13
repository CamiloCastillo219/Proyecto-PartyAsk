package com.example.partyask

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val createAccountTextView = findViewById<TextView>(R.id.create_account)
        createAccountTextView.setOnClickListener {
            val intent = Intent(this, CreateAnAcount::class.java)
            startActivity(intent)
        }

        val LogInCountTextView = findViewById<TextView>(R.id.log_in)
        LogInCountTextView.setOnClickListener {
            val intent = Intent(this, LogInCount::class.java)
            startActivity(intent)
        }
    }
}
