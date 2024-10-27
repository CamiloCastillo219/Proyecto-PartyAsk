package com.example.partyask

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CreateAnAcount : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_an_account)

        // Inicializa Firebase Auth
        auth = FirebaseAuth.getInstance()

        val usernameField = findViewById<EditText>(R.id.username)
        val emailField = findViewById<EditText>(R.id.email)
        val passwordField = findViewById<EditText>(R.id.password)
        val conpasswordField = findViewById<EditText>(R.id.conpassword)
        val createAccountButton = findViewById<TextView>(R.id.register)
        val backToMenuImageView = findViewById<ImageView>(R.id.backmain)
        val goTermsAndCondition = findViewById<TextView>(R.id.term_and_condict)

        // Configura los campos para borrar el texto de ayuda al hacer clic y restaurarlo si se dejan vacíos
        setupField(usernameField, getString(R.string.username))
        setupField(emailField, getString(R.string.email))

        // Configura las contraseñas con formato de asteriscos (****)
        setupPasswordField(passwordField, getString(R.string.password))
        setupPasswordField(conpasswordField, getString(R.string.conpassword))

        backToMenuImageView.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        goTermsAndCondition.setOnClickListener {
            startActivity(Intent(this, termsCondition::class.java))
        }

        createAccountButton.setOnClickListener {
            val username = usernameField.text.toString()
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            val conpassword = conpasswordField.text.toString()

            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && conpassword.isNotEmpty() && (password == conpassword)) {
                createAccount(username, email, password)
            } else {
                Toast.makeText(this, "Error al crear la cuenta, inicie otra vez", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupField(field: EditText, hintText: String) {
        field.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (field.text.toString() == hintText) {
                    field.setText("")
                }
            } else {
                if (field.text.toString().isEmpty()) {
                    field.setText(hintText)
                }
            }
        }
    }

    private fun setupPasswordField(field: EditText, hintText: String) {
        field.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        field.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (field.text.toString() == hintText) {
                    field.setText("")
                }
            } else {
                if (field.text.toString().isEmpty()) {
                    field.setText(hintText)
                }
            }
        }
    }

    // Define la clase User para almacenar los datos en la base de datos
    data class User(
        val username: String? = null,
        val email: String? = null
    )

    private fun createAccount(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Usuario registrado correctamente
                    val userId = auth.currentUser?.uid
                    userId?.let {
                        saveUserToDatabase(it, username, email)
                        Toast.makeText(this, "Account Created for $username", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, HomePage::class.java))
                    }
                } else {
                    // Muestra el error en el registro
                    Toast.makeText(this, "Error in registration: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserToDatabase(userId: String, username: String, email: String) {
        // Referencia a la base de datos
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")

        // Crea el objeto User con la información del usuario
        val user = User(username, email)

        // Guarda el usuario en la base de datos con su userId
        usersRef.child(userId).setValue(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "User saved to database", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to save user: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
