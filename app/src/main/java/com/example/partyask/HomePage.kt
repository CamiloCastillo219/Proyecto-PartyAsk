package com.example.partyask
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val goToCodepassing = findViewById<TextView>(R.id.join_to_party)
        goToCodepassing.setOnClickListener {
            val intent = Intent(this, CodePass::class.java)
            startActivity(intent)
        }

        val goToCodeName = findViewById<TextView>(R.id.HostParty)
        goToCodeName.setOnClickListener {
            val intent = Intent(this, HostName::class.java)
            startActivity(intent)
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