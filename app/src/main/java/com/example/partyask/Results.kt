package com.example.partyask

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Results : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private lateinit var playerTextViews: List<TextView>
    private lateinit var progressBar: ProgressBar
    private var questionCount = 0
    private var roomCode = ""
    private var votes: Map<String, Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        // Initialize views
        playerTextViews = listOf(
            findViewById(R.id.resultplayer1),
            findViewById(R.id.resultplayer2),
            findViewById(R.id.resultplayer3),
            findViewById(R.id.resultplayer4)
        )
        progressBar = findViewById(R.id.progressBar2)

        // Recibir valores desde Question
        roomCode = intent.getStringExtra("ROOM_CODE") ?: ""
        questionCount = intent.getIntExtra("QUESTION_COUNT", 0)
        votes = intent.getSerializableExtra("VOTES_MAP") as? Map<String, Int>

        // Mostrar votos y realizar la cuenta regresiva
        displayVotes()
        startCountdown()
    }

    private fun displayVotes() {
        var index = 0
        votes?.forEach { (player, voteCount) ->
            if (index < playerTextViews.size) {
                playerTextViews[index].apply {
                    text = "$player : $voteCount votos"
                    visibility = View.VISIBLE
                }
                index++
            }
        }
    }

    private fun startCountdown() {
        progressBar.visibility = View.VISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            checkAndNavigate()
        }, 6000)
    }

    private fun checkAndNavigate() {
        val partySettingsRef = database.getReference("rooms/$roomCode/PartySettings")
        val timePartyRef = partySettingsRef.child("timeParty")
        val partyLeftRef = partySettingsRef.child("partyLeft")

        timePartyRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val timeParty = snapshot.getValue(Int::class.java) ?: 0

                partyLeftRef.runTransaction(object : com.google.firebase.database.Transaction.Handler {
                    override fun doTransaction(currentData: com.google.firebase.database.MutableData): com.google.firebase.database.Transaction.Result {
                        val currentPartyLeft = currentData.getValue(Int::class.java) ?: 0
                        currentData.value = currentPartyLeft + 1
                        return com.google.firebase.database.Transaction.success(currentData)
                    }

                    override fun onComplete(error: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {
                        if (error != null) {
                            Toast.makeText(this@Results, "Error updating game state", Toast.LENGTH_SHORT).show()
                            return
                        }

                        val partyLeft = currentData?.getValue(Int::class.java) ?: 1
                        if (partyLeft >= timeParty) {
                            val intent = Intent(this@Results, FinPartida::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // Navega a CategoryLoad en lugar de Question
                            val intent = Intent(this@Results, CategoryLoad::class.java)
                            intent.putExtra("ROOM_CODE", roomCode)
                            startActivity(intent)
                            finish()
                        }
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Results, "Error loading game settings", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
