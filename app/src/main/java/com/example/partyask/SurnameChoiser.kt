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

class SurnameChoiser : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surname_code_maker)

        val playerNameEditText = findViewById<EditText>(R.id.codewriter)
        val joinButton = findViewById<TextView>(R.id.Enter_to_party_invite)

        playerNameEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && playerNameEditText.text.toString() == "- - - - - -") {
                playerNameEditText.text.clear()
            }
        }

        // Obtenemos el código de la sala desde el intent
        val roomCode = intent.getStringExtra("ROOM_CODE")

        val backtomenu_scImageView = findViewById<ImageView>(R.id.backmain_sc)
        backtomenu_scImageView.setOnClickListener {
            val intent = Intent(this, CodePass::class.java)
            startActivity(intent)
        }

        joinButton.setOnClickListener {
            val playerName = playerNameEditText.text.toString()

            if (playerName.isNotEmpty() && roomCode != null) {
                verificarYUnirJugador(roomCode, playerName)
            } else {
                Toast.makeText(this, "Por favor ingresa un nombre válido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para verificar el número de jugadores y asignar el siguiente lugar disponible
    private fun verificarYUnirJugador(roomCode: String, playerName: String) {
        val playersRef = database.getReference("rooms").child(roomCode).child("jugadores")

        playersRef.get().addOnSuccessListener { snapshot ->
            val playerCount = snapshot.childrenCount.toInt()

            if (playerCount < 4) {
                // Determinamos el próximo jugador (player2, player3, o player4)
                val nextPlayerKey = "player${playerCount + 1}"
                playersRef.child(nextPlayerKey).child("nickname").setValue(playerName)  // Guardamos el nombre en el subcampo "nickname"
                    .addOnSuccessListener {
                        Toast.makeText(this, "Bienvenido al juego, $playerName", Toast.LENGTH_LONG).show()

                        // Redirige a LobbyGame y envía el nombre del jugador para actualizar el TextView correspondiente
                        val intent = Intent(this, LobbyGame::class.java)
                        intent.putExtra("PLAYER_NAME", playerName)
                        intent.putExtra("ROOM_CODE", roomCode)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al unirse a la partida", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // Si ya hay 4 jugadores, mostramos un mensaje de error
                Toast.makeText(this, "Máximo de jugadores alcanzado", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error al verificar la cantidad de jugadores", Toast.LENGTH_SHORT).show()
        }
    }
}

