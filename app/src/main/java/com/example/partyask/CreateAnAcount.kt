package com.example.partyask

import android.content.Intent
import android.os.Bundle
import android.view.View
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
        usernameField.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (usernameField.text.toString() == getString(R.string.username)) {
                    usernameField.setText("")
                }
            } else {
                if (usernameField.text.toString().isEmpty()) {
                    usernameField.setText(getString(R.string.username))
                }
            }
        }
        val emailField = findViewById<EditText>(R.id.email)
        emailField.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (emailField.text.toString() == getString(R.string.email)) {
                    emailField.setText("")
                }
            } else {
                if (emailField.text.toString().isEmpty()) {
                    emailField.setText(getString(R.string.email))
                }
            }
        }
        val passwordField = findViewById<EditText>(R.id.password)
        passwordField.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (passwordField.text.toString() == getString(R.string.password)) {
                    passwordField.setText("")
                }
            } else {
                if (passwordField.text.toString().isEmpty()) {
                    passwordField.setText(getString(R.string.password))
                }
            }
        }
        val conpasswordField = findViewById<EditText>(R.id.conpassword)
        conpasswordField.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (conpasswordField.text.toString() == getString(R.string.conpassword)) {
                    conpasswordField.setText("")
                }
            } else {
                if (conpasswordField.text.toString().isEmpty()) {
                    conpasswordField.setText(getString(R.string.conpassword))
                }
            }
        }
        val createAccountButton = findViewById<TextView>(R.id.register)



        val backtomenuImageView = findViewById<ImageView>(R.id.backmain)
        backtomenuImageView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val goTermsAndCondition = findViewById<TextView>(R.id.term_and_condict)
        goTermsAndCondition.setOnClickListener {
            val intent = Intent(this, termsCondition::class.java)
            startActivity(intent)
        }

        val usernameEditText = findViewById<EditText>(R.id.username)

// Listener para borrar el texto cuando se selecciona
        usernameEditText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                // Si el campo tiene el foco, borrar el texto
                if (usernameEditText.text.toString() == getString(R.string.username)) {
                    usernameEditText.setText("")
                }
            } else {
                // Si el campo pierde el foco y está vacío, restaurar el texto
                if (usernameEditText.text.toString().isEmpty()) {
                    usernameEditText.setText(getString(R.string.username))
                }
            }
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