package com.example.partyask

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.database.FirebaseDatabase

class ChosseModes : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val selectedModes = mutableListOf<String>()
    private lateinit var timePartyEditText: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chosse_modes)

        timePartyEditText = findViewById(R.id.Time_party)

        // Configuración para ocultar el texto y validar entrada numérica
        timePartyEditText.setOnClickListener {
            timePartyEditText.text.clear() // Limpia el texto al hacer clic
        }

        val modeButtons = listOf(
            findViewById<TextView>(R.id.Guess_who) to "historia_o_mentira",
            findViewById<TextView>(R.id.Most_propably) to "quien_es_mas_probable",
            findViewById<TextView>(R.id.true_false) to "verdadero_falso",
            findViewById<TextView>(R.id.free_questions) to "preguntas_libres"
        )

        // Configuración de botones de modo
        for ((button, mode) in modeButtons) {
            button.setOnClickListener {
                toggleModeSelection(button, mode)
            }
        }

        // Botón de confirmación
        val confirmButton = findViewById<TextView>(R.id.readyoptions)
        confirmButton.setOnClickListener {
            saveSettingsToFirebase()
            Toast.makeText(this, "Configuración de partida actualizada", Toast.LENGTH_SHORT).show()
        }
    }

    // Cambia el estado de selección del botón y su fondo
    private fun toggleModeSelection(button: TextView, mode: String) {
        val isSelected = selectedModes.contains(mode)

        if (isSelected) {
            selectedModes.remove(mode)
            button.background = ContextCompat.getDrawable(this, R.drawable.grey_button_background)
        } else {
            selectedModes.add(mode)
            button.background = ContextCompat.getDrawable(this, R.drawable.selected_button_background)
        }
    }

    // Genera la lista de preguntas ordenadas según las selecciones del usuario
    private fun generateQuestionList(): List<Pair<String, Int>> {
        val questionList = mutableListOf<Pair<String, Int>>()
        val numberOfQuestions = timePartyEditText.text.toString().toIntOrNull() ?: return questionList

        val questionsPerCategory = numberOfQuestions / selectedModes.size
        var remainingQuestions = numberOfQuestions

        for (mode in selectedModes) {
            for (i in 1..questionsPerCategory) {
                questionList.add(Pair(mode, i))
                remainingQuestions--
                if (remainingQuestions <= 0) break
            }
            if (remainingQuestions <= 0) break
        }

        var index = 0
        while (remainingQuestions > 0) {
            questionList.add(Pair(selectedModes[index % selectedModes.size], questionsPerCategory + 1))
            remainingQuestions--
            index++
        }

        return questionList
    }

    // Guarda la configuración en Firebase y navega de vuelta a LobbyGame
    private fun saveSettingsToFirebase() {
        val timePartyText = timePartyEditText.text.toString()
        if (timePartyText.isEmpty()) {
            timePartyEditText.error = "Por favor ingresa el número de preguntas."
            return
        }

        val numberOfQuestions = timePartyText.toIntOrNull()
        if (numberOfQuestions == null) {
            timePartyEditText.error = "Por favor ingresar solo valores numéricos."
            return
        }

        val roomCode = intent.getStringExtra("ROOM_CODE") ?: ""

        val questionList = generateQuestionList()

        // Guardar configuración de modos y número de preguntas
        val settingsRef = database.reference.child("rooms").child(roomCode).child("PartySettings")
        val settingsData = mapOf(
            "selectedModes" to selectedModes,
            "timeParty" to numberOfQuestions
        )

        settingsRef.updateChildren(settingsData).addOnCompleteListener {
            if (it.isSuccessful) {
                // Subir la lista de preguntas a Firebase
                subirListaAPartidaFirebase(roomCode, questionList)

                // Cambiar a LobbyGame después de guardar
                val intent = Intent(this, LobbyGame::class.java)
                intent.putExtra("ROOM_CODE", roomCode)
                startActivity(intent)
                finish()
            } else {
                timePartyEditText.error = "Error al guardar la configuración. Inténtalo de nuevo."
            }
        }
    }

    // Función para subir la lista de preguntas a Firebase
    private fun subirListaAPartidaFirebase(codigoPartida: String, listaPreguntas: List<Pair<String, Int>>) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("rooms/$codigoPartida/listaPreguntas")

        // Convertir la lista de preguntas en una lista de mapas para Firebase
        val listaMap = listaPreguntas.map { mapOf("categoria" to it.first, "preguntaIndex" to it.second) }

        databaseRef.setValue(listaMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Firebase", "Lista de preguntas subida correctamente.")
            } else {
                Log.e("Firebase", "Error al subir la lista de preguntas: ${task.exception}")
            }
        }
    }
}
