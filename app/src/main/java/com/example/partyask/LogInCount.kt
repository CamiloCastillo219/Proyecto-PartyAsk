package com.example.partyask

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LogInCount : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in_count)

        // Inicializa Firebase Auth
        auth = FirebaseAuth.getInstance()

        val emailField = findViewById<EditText>(R.id.username)
        val passwordField = findViewById<EditText>(R.id.password)
        passwordField.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        val loginAccountButton = findViewById<TextView>(R.id.register)
        val backToMenuImageView = findViewById<ImageView>(R.id.backmain)
        val makeAccountTextView = findViewById<TextView>(R.id.create_account)

        // Borrar el texto de email cuando el campo gana o pierde el foco
        emailField.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus && emailField.text.toString() == getString(R.string.username)) {
                emailField.setText("")
            } else if (!hasFocus && emailField.text.toString().isEmpty()) {
                emailField.setText(getString(R.string.username))
            }
        }

        // Borrar el texto de password cuando el campo gana o pierde el foco
        passwordField.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus && passwordField.text.toString() == getString(R.string.password)) {
                passwordField.setText("")
            } else if (!hasFocus && passwordField.text.toString().isEmpty()) {
                passwordField.setText(getString(R.string.password))
            }
        }

        // Navegar al MainActivity
        backToMenuImageView.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // Navegar a la pantalla de creación de cuenta
        makeAccountTextView.setOnClickListener {
            startActivity(Intent(this, CreateAnAcount::class.java))
        }

        // Iniciar sesión con Firebase Authentication
        loginAccountButton.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso, navega a la pantalla principal
                    Toast.makeText(this, "Welcome back!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, HomePage::class.java))
                } else {
                    // Si el inicio de sesión falla, muestra un mensaje de error
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
