package com.example.partyask

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CreateAnAcount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_an_account)

        val usernameField = findViewById<EditText>(R.id.username)
        val emailField = findViewById<EditText>(R.id.email)
        val passwordField = findViewById<EditText>(R.id.password)
        val conpasswordField = findViewById<EditText>(R.id.conpassword)
        val createAccountButton = findViewById<TextView>(R.id.register)

        val backtomenuImageView = findViewById<ImageView>(R.id.backmain)
        backtomenuImageView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        createAccountButton.setOnClickListener {
            val username = usernameField.text.toString()
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            val conpassword = conpasswordField.text.toString()

            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && conpassword.isNotEmpty() && (password == conpassword)) {
                // Lógica para crear la cuenta
                Toast.makeText(this, "Account Created for $username", Toast.LENGTH_LONG).show()
                val intent = Intent(this, HomePage::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}