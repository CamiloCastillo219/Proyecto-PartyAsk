package com.example.partyask

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LobbyGame : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private lateinit var playerTextViews: List<TextView> // Lista para los TextViews de los jugadores

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby_game)

        // Referencias a los TextViews de los jugadores
        playerTextViews = listOf(
            findViewById(R.id.player1),
            findViewById(R.id.player2),
            findViewById(R.id.player3),
            findViewById(R.id.player4)
        )

        val randomText = findViewById<TextView>(R.id.randomtext)

        // Obtener el código de sala desde el Intent
        val roomCode = intent.getStringExtra("ROOM_CODE") ?: ""

        // Log para verificar el valor de roomCode
        Log.d("LobbyGame", "Room Code: $roomCode")

        // Mostrar el código de sala en el TextView randomtext
        randomText.text = "Room code: $roomCode"

        // Escuchar cambios en los jugadores para actualizar la UI
        escucharCambiosEnJugadores(roomCode)
    }

    private fun escucharCambiosEnJugadores(roomCode: String) {
        val jugadoresRef = database.getReference("rooms/$roomCode/jugadores")

        // Escuchar cambios en la lista de jugadores
        jugadoresRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Ocultar todos los TextViews al inicio
                for (textView in playerTextViews) {
                    textView.visibility = View.GONE
                }

                // Iterar a través de los jugadores en el snapshot
                var index = 0
                for (playerSnapshot in snapshot.children) {
                    if (index < playerTextViews.size) {
                        // Obtener el nickname del jugador
                        val nickname = playerSnapshot.child("nickname").getValue(String::class.java) ?: "Player${index + 1}"

                        // Mostrar el TextView correspondiente y asignar el nombre
                        playerTextViews[index].text = nickname
                        playerTextViews[index].visibility = View.VISIBLE
                        index++
                    }
                }

                // Si hay más de 4 jugadores, mostrar mensaje de límite alcanzado
                if (index >= 4) {
                    Toast.makeText(this@LobbyGame, "Máximo de jugadores alcanzado", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LobbyGame, "Error al escuchar los jugadores", Toast.LENGTH_SHORT).show()
                Log.e("LobbyGame", "Error al escuchar jugadores en Firebase", error.toException())
            }
        })
    }
}
