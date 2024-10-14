package com.example.partyask

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LogInCount : AppCompatActivity() {
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in_count)

        val usernameField = findViewById<EditText>(R.id.username)
        val passwordField = findViewById<EditText>(R.id.password)
        val loginAccountButton = findViewById<TextView>(R.id.register)

        val backtomenuImageView = findViewById<ImageView>(R.id.backmain)
        backtomenuImageView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val MakeAccountTextView = findViewById<TextView>(R.id.create_account)
        MakeAccountTextView.setOnClickListener {
            val intent = Intent(this, CreateAnAcount::class.java)
            startActivity(intent)
        }

        loginAccountButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Lógica para entrar a una cuenta
                Toast.makeText(this, "Wellcome $username", Toast.LENGTH_LONG).show()
                val intent = Intent(this, HomePage::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}