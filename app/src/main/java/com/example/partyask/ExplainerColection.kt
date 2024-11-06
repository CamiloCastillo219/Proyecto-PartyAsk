package com.example.partyask

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ExplainerColection : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private lateinit var questionTextViews: List<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explainer_colection)

        // Referenciamos los TextViews desde el layout
        questionTextViews = listOf(
            findViewById(R.id.empty1),
            findViewById(R.id.empty2),
            findViewById(R.id.empty3),
            findViewById(R.id.empty4)
        )

        // Cargar las preguntas de Firebase y asignarlas a los botones
        loadQuestionsFromFirebase()
    }

    private fun loadQuestionsFromFirebase() {
        // Reemplaza "questions" con la ruta correcta en Firebase
        val categoryName = intent.getStringExtra("category_name") ?: "default_category"
        val questionsRef = database.getReference("questions/$categoryName")

        questionsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val questions = snapshot.children.mapNotNull { it.getValue(String::class.java) }

                // Asigna los fragmentos de las preguntas a los TextViews y configura los listeners
                questions.forEachIndexed { index, question ->
                    if (index < questionTextViews.size) {
                        val textView = questionTextViews[index]
                        val previewText = if (question.length > 20) question.take(20) + "..." else question
                        textView.text = previewText
                        textView.setOnClickListener {
                            Toast.makeText(this@ExplainerColection, question, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ExplainerColection, "Error al cargar preguntas", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
