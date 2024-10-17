package com.example.partyask

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class termsCondition : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.termsandconditions)

        val backtomenuImageView = findViewById<ImageView>(R.id.backmain)
        backtomenuImageView.setOnClickListener {
            val intent = Intent(this, CreateAnAcount::class.java)
            startActivity(intent)
        }
        // Aquí puedes añadir el código para inicializar vistas o listeners
    }
}