package com.example.partyask

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val goToCodepassing = findViewById<TextView>(R.id.join_to_party)
        goToCodepassing.setOnClickListener {
            val intent = Intent(this, CodePass::class.java)
            startActivity(intent)
        }
        // Aquí puedes añadir el código para inicializar vistas o listeners
    }
}