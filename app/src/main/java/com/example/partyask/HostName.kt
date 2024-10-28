package com.example.partyask

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class HostName : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private lateinit var roomCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host_name)

        val codepassing = findViewById<EditText>(R.id.codewriter)
        val enterToPartyButton = findViewById<TextView>(R.id.Enter_to_party)
        val backToMenuImageView = findViewById<ImageView>(R.id.backmain)

        // Generamos un código de sala aleatorio
        roomCode = generateRoomCode()

        // Configuramos el comportamiento del EditText para borrar el texto predeterminado al enfocarse
        codepassing.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && codepassing.text.toString() == "- - - - - -") {
                codepassing.text.clear()
            }
        }

        // Botón para regresar al menú principal
        backToMenuImageView.setOnClickListener {
            startActivity(Intent(this, HomePage::class.java))
        }

        // Botón para crear la sala y pasar a la Activity de LobbyGame
        enterToPartyButton.setOnClickListener {
            val playerName = codepassing.text.toString()
            if (playerName.isNotEmpty()) {
                Toast.makeText(this, "Bienvenido al juego $playerName", Toast.LENGTH_LONG).show()

                // Guardamos el nombre del host en Firebase bajo el código de sala generado
                val playerRef = database.getReference("rooms/$roomCode/jugadores/player1/nickname")
                playerRef.setValue(playerName)
                    .addOnSuccessListener {
                        // Redirigimos a LobbyGame pasando el código de sala y el nombre del host
                        val intent = Intent(this, LobbyGame::class.java)
                        intent.putExtra("ROOM_CODE", roomCode) // Enviamos el código de sala a LobbyGame
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al crear la sala", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Nombre no válido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Método para generar un código de sala aleatorio de 6 caracteres
    private fun generateRoomCode(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..6)
            .map { chars.random() }
            .joinToString("")
    }
}


