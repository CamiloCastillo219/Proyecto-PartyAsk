package com.example.partyask

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class FinPartida : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fin_partida)

        progressBar = findViewById(R.id.progressBar2)
        progressBar.max = 100

        // Simulación de carga durante 5 segundos
        val totalTime = 5000L // 5 segundos
        val updateInterval = 50L
        var progress = 0

        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                progress += (100 * updateInterval / totalTime).toInt()
                if (progress <= 100) {
                    progressBar.progress = progress
                    handler.postDelayed(this, updateInterval)
                } else {
                    // Redirigir a "HomePage" después de los 5 segundos
                    startActivity(Intent(this@FinPartida, HomePage::class.java))
                    finish()
                }
            }
        }
        handler.post(runnable)
    }
}
