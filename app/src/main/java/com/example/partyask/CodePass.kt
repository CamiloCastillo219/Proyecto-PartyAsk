package com.example.partyask

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class CodePass : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_pass)

        val codepassing = findViewById<EditText>(R.id.codewriter)
        val passatravez = findViewById<TextView>(R.id.pastthrough)

        codepassing.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && codepassing.text.toString() == "- - - - - -") {
                codepassing.text.clear()
            }
        }

        // Botón para volver al menú principal
        val backtomenuImageView = findViewById<ImageView>(R.id.backmain)
        backtomenuImageView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Botón para iniciar sesión
        val LogInCountTextView = findViewById<TextView>(R.id.log_in)
        LogInCountTextView.setOnClickListener {
            val intent = Intent(this, LogInCount::class.java)
            startActivity(intent)
        }

        // Al hacer clic en el botón de pasar a través
        passatravez.setOnClickListener {
            val codelog = codepassing.text.toString()

            if (codelog.isNotEmpty()) {
                verificarCodigoSala(codelog) // Llama a la función para verificar el código en Firebase
            } else {
                Toast.makeText(this, "Por favor ingresa un código", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para verificar si el código existe en Firebase
    private fun verificarCodigoSala(codigo: String) {
        val roomRef = database.getReference("rooms").child(codigo)

        roomRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                // Si el código de sala existe, pasa a la siguiente Activity
                Toast.makeText(this, "Uniéndose a partida", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SurnameChoiser::class.java)
                intent.putExtra("ROOM_CODE", codigo) // Envía el código de la sala a SurnameChooser
                startActivity(intent)
            } else {
                // Si el código no existe, muestra un mensaje
                Toast.makeText(this, "No existe una sala con ese código", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error al verificar el código", Toast.LENGTH_SHORT).show()
        }
    }
}
