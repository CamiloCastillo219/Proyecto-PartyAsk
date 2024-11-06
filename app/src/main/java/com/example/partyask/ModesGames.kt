package com.example.partyask
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class ModesGames : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_modes)

        findViewById<TextView>(R.id.Guess_who).setOnClickListener {
            openDetailActivity("guess_who")
        }

        findViewById<TextView>(R.id.Most_propably).setOnClickListener {
            openDetailActivity("most_probably")
        }

        findViewById<TextView>(R.id.true_false).setOnClickListener {
            openDetailActivity("true_false")
        }

        findViewById<TextView>(R.id.free_questions).setOnClickListener {
            openDetailActivity("free_questions")
        }

        findViewById<TextView>(R.id.create_new).setOnClickListener {
            openDetailActivity("history_or_lie")
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
    private fun openDetailActivity(mode: String) {
        val intent = Intent(this, ModeExplain::class.java)
        intent.putExtra("mode_key", mode)
        startActivity(intent)
    }

}