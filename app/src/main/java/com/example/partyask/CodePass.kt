package com.example.partyask

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CodePass : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_pass)

        val codepassing = findViewById<EditText>(R.id.codewriter)
        val passatravez = findViewById<TextView>(R.id.pastthrough)

        val backtomenuImageView = findViewById<ImageView>(R.id.backmain)
        backtomenuImageView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val LogInCountTextView = findViewById<TextView>(R.id.log_in)
        LogInCountTextView.setOnClickListener {
            val intent = Intent(this, LogInCount::class.java)
            startActivity(intent)
        }

        passatravez.setOnClickListener {
            val codelog = codepassing.text.toString()

            if (codelog.isNotEmpty()) {
                Toast.makeText(this, "Redireccion de login un en desarrollo", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(this, HomePage::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Codigo no valido", Toast.LENGTH_SHORT).show()
            }
        }
    }
}