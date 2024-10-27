package com.example.partyask
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class Settings : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val loginback = findViewById<TextView>(R.id.back_to_log)
        loginback.setOnClickListener{
            startActivity(Intent(this, LogInCount::class.java))
        }

        val goterms = findViewById<TextView>(R.id.terminos_condiciones)
        goterms.setOnClickListener{
            startActivity(Intent(this, termsCondition::class.java))
        }

        // Configuración del BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomePage::class.java))
                    true
                }
                R.id.nav_files -> {
                    startActivity(Intent(this, FilesPage::class.java))
                    true
                }
                R.id.nav_notifications -> {
                    startActivity(Intent(this, ModesGames::class.java))
                    true
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, Settings::class.java))
                    true
                }
                else -> false
            }
        }
    }
}