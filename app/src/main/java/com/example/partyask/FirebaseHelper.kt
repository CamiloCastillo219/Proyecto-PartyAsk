package com.example.partyask

import com.google.firebase.database.FirebaseDatabase

class FirebaseHelper {

    private val database = FirebaseDatabase.getInstance()

    // Función para agregar una pregunta a una categoría específica
    fun agregarPregunta(categoria: String, pregunta: String) {
        val preguntasRef = database.getReference("pot_de_preguntas").child(categoria)
        val preguntaId = preguntasRef.push().key

        if (preguntaId != null) {
            val preguntaData = mapOf(
                "preguntaId" to preguntaId,
                "pregunta" to pregunta,
                "categoria" to categoria
            )
            preguntasRef.child(preguntaId).setValue(preguntaData)
                .addOnSuccessListener {
                    println("Pregunta guardada exitosamente en la categoría $categoria.")
                }
                .addOnFailureListener { e ->
                    println("Error al guardar la pregunta: ${e.message}")
                }
        }
    }

    // Función para obtener preguntas de una categoría específica
    fun obtenerPreguntasPorCategoria(categoria: String, callback: (List<String>) -> Unit) {
        val preguntasRef = database.getReference("pot_de_preguntas").child(categoria)

        preguntasRef.get().addOnSuccessListener { snapshot ->
            val listaPreguntas = mutableListOf<String>()
            for (preguntaSnapshot in snapshot.children) {
                val pregunta = preguntaSnapshot.child("pregunta").value.toString()
                listaPreguntas.add(pregunta)
            }
            callback(listaPreguntas)
        }.addOnFailureListener { e ->
            println("Error al obtener las preguntas: ${e.message}")
        }
    }
}
