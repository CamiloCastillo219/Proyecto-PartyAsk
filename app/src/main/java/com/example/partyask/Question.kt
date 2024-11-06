package com.example.partyask

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.random.Random

class Question : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private lateinit var questionTextView: TextView
    private lateinit var playerTextViews: List<TextView>
    private lateinit var progressBar: ProgressBar

    private val votes = mutableMapOf<String, Int>() // Para almacenar votos de cada jugador
    private var questionCount = 0 // Para contar las preguntas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions_shower)

        supportActionBar?.title = "Pregunta"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        questionTextView = findViewById(R.id.question_empty)
        progressBar = findViewById(R.id.progressBar3)

        playerTextViews = listOf(
            findViewById(R.id.player1),
            findViewById(R.id.player2),
            findViewById(R.id.player3),
            findViewById(R.id.player4)
        )

        val roomCode = intent.getStringExtra("ROOM_CODE") ?: ""
        val categoryName = intent.getStringExtra("CATEGORY_NAME") ?: "Default Category"

        cargarPreguntaAleatoria(categoryName)
        escucharCambiosEnJugadores(roomCode)
        iniciarProgreso(7000)

        // Asignar listener de clic a cada TextView de jugador para contar votos
        for (textView in playerTextViews) {
            textView.setOnClickListener {
                Log.d("Question", "Click en ${textView.text}")
                votarJugador(textView.text.toString())
            }
        }
    }

    private fun cargarPreguntaAleatoria(categoryName: String) {
        val preguntaRef = database.getReference("pozoPreguntas/$categoryName")

        preguntaRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                questionCount = snapshot.childrenCount.toInt()
                if (questionCount > 0) {
                    val randomIndex = Random.nextInt(1, questionCount + 1)
                    val selectedQuestionRef = preguntaRef.child(randomIndex.toString()).child("texto")

                    selectedQuestionRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(questionSnapshot: DataSnapshot) {
                            val questionText = questionSnapshot.getValue(String::class.java)
                            questionTextView.text = questionText ?: "No hay preguntas en esta categoría."
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("Question", "Error al cargar la pregunta", error.toException())
                            questionTextView.text = "Error al cargar la pregunta."
                        }
                    })
                } else {
                    questionTextView.text = "No hay preguntas en esta categoría."
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Question", "Error al cargar la pregunta aleatoria", error.toException())
                questionTextView.text = "Error al cargar la pregunta."
            }
        })
    }

    private fun votarJugador(playerName: String) {
        if (playerName.isEmpty()) {
            Log.e("Question", "Nombre del jugador está vacío. No se registrará voto.")
            return
        }

        val currentVotes = votes[playerName] ?: 0
        votes[playerName] = currentVotes + 1
        Log.d("Question", "Voto registrado para $playerName: ${votes[playerName]} votos")
    }

    private fun escucharCambiosEnJugadores(roomCode: String) {
        val jugadoresRef = database.getReference("rooms/$roomCode/jugadores")

        jugadoresRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (textView in playerTextViews) {
                    textView.visibility = View.GONE
                }

                var index = 0
                for (playerSnapshot in snapshot.children) {
                    if (index < playerTextViews.size) {
                        val nickname = playerSnapshot.child("nickname").getValue(String::class.java) ?: "Player${index + 1}"
                        playerTextViews[index].text = nickname
                        playerTextViews[index].visibility = View.VISIBLE
                        index++
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Question", "Error al escuchar los jugadores", error.toException())
            }
        })
    }

    private fun iniciarProgreso(duracion: Long) {
        val interval = 100
        val totalIntervals = (duracion / interval).toInt()
        val increment = 100 / totalIntervals

        var progress = 0
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed(object : Runnable {
            override fun run() {
                val roomCode = intent.getStringExtra("ROOM_CODE") ?: ""
                progressBar.progress = progress
                progress += increment
                if (progress <= 100) {
                    handler.postDelayed(this, interval.toLong())
                } else {
                    pasarASiguienteActividad(roomCode) // Pasar a la siguiente Activity con roomCode
                }
            }
        }, interval.toLong())
    }

    private fun pasarASiguienteActividad(roomCode: String) {
        Log.d("Question", "Votos antes de pasar a Results: $votes")

        val intent = Intent(this, Results::class.java)
        intent.putExtra("VOTES_MAP", HashMap(votes)) // Pasar votos como HashMap
        intent.putExtra("QUESTION_COUNT", questionCount) // Pasar questionCount
        intent.putExtra("ROOM_CODE", roomCode) // Pasar roomCode
        startActivity(intent)
        finish()
    }
}
