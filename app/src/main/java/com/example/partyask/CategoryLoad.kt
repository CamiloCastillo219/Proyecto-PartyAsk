package com.example.partyask

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.random.Random

class CategoryLoad : AppCompatActivity() {

    private lateinit var categoryEmptyTextView: TextView
    private lateinit var progressBar: ProgressBar
    private val database = FirebaseDatabase.getInstance()
    private lateinit var codeRoom: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_load)

        categoryEmptyTextView = findViewById(R.id.category_empty)
        progressBar = findViewById(R.id.progressBar2)

        // Obtiene el código de la sala desde la Intent
        codeRoom = intent.getStringExtra("ROOM_CODE") ?: ""

        // Verifica si codeRoom está vacío y muestra un error si es necesario
        if (codeRoom.isEmpty()) {
            Toast.makeText(this, "Código de sala no recibido", Toast.LENGTH_SHORT).show()
            finish() // Finaliza la actividad si no se recibe el roomCode
            return
        }

        // Log para verificar el valor de codeRoom
        Log.d("CategoryLoad", "Received codeRoom: $codeRoom")

        // Cargar una categoría aleatoria de Firebase y mostrarla
        loadRandomCategoryFromFirebase()
    }

    private fun loadRandomCategoryFromFirebase() {
        // Referencia a la ruta de selectedModes en Firebase
        val selectedModesRef = database.getReference("rooms/$codeRoom/PartySettings/selectedModes")

        selectedModesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Limpiar la lista antes de agregar las categorías nuevamente
                val categories = mutableListOf<String>()

                // Recorre los elementos en selectedModes y los agrega a la lista
                for (categorySnapshot in snapshot.children) {
                    val categoryName = categorySnapshot.getValue(String::class.java)
                    if (categoryName != null) {
                        categories.add(categoryName)
                    }
                }

                // Verifica si la lista tiene elementos
                if (categories.isNotEmpty()) {
                    // Selecciona una categoría aleatoria de la lista
                    val randomCategory = categories[Random.nextInt(categories.size)]
                    categoryEmptyTextView.text = randomCategory

                    // Pasa el nombre de la categoría seleccionada a la siguiente Activity
                    val intent = Intent(this@CategoryLoad, Question::class.java)
                    intent.putExtra("ROOM_CODE", codeRoom)
                    intent.putExtra("CATEGORY_NAME", randomCategory)
                    startProgressWithDelay(intent)
                } else {
                    // Si no hay categorías, muestra un mensaje de "No disponible"
                    Toast.makeText(this@CategoryLoad, "No hay categorías disponibles", Toast.LENGTH_SHORT).show()
                    categoryEmptyTextView.text = "Categoría no disponible"
                    startProgressWithDelay(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Maneja el error en la lectura de Firebase
                Toast.makeText(this@CategoryLoad, "Error al cargar categorías", Toast.LENGTH_SHORT).show()
                categoryEmptyTextView.text = "Error al cargar"
                startProgressWithDelay(null)
            }
        })
    }

    private fun startProgressWithDelay(intent: Intent?) {
        Handler(Looper.getMainLooper()).postDelayed({
            if (intent != null) {
                startActivity(intent)
            }
            finish()
        }, 3000)
    }

    override fun onResume() {
        super.onResume()
        // Recargar la categoría aleatoria cada vez que la actividad sea visible
        loadRandomCategoryFromFirebase()
    }
}

