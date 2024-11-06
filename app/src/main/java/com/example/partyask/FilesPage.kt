package com.example.partyask

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class FilesPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_files)

        // Botones de categorías
        val guessWho = findViewById<TextView>(R.id.Guess_who)  // Ejemplo de otro botón

        // Configura cada botón para enviar la categoría correspondiente
        guessWho.setOnClickListener {
            navigateToExplainerCollection(getString(R.string.guess_who_basics))
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

    // Función para iniciar ExplainerCollection con la categoría seleccionada
    private fun navigateToExplainerCollection(categoryName: String) {
        val intent = Intent(this, ExplainerColection::class.java)
        intent.putExtra("category_name", categoryName)
        startActivity(intent)
    }
}
