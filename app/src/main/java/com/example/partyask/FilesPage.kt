package com.example.partyask
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class FilesPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_files)

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