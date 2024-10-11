package com.example.partyask

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.LinearGradient
import android.graphics.Shader
import android.widget.TextView
import android.graphics.Paint

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gradientTextView = findViewById<TextView>(R.id.partyask) // Cambiaste el ID aquí

        val paint: Paint = gradientTextView.paint
        val width = paint.measureText(gradientTextView.text.toString())
        val textShader: Shader = LinearGradient(
            0f, 0f, width, gradientTextView.textSize,
            intArrayOf(
                getColor(R.color.purple), // Morado (#570F69)
                getColor(R.color.pink)    // Rosado (#AC1ECF)
            ),
            floatArrayOf(0.5f, 1f), // 50% morado, 50% rosado
            Shader.TileMode.CLAMP
        )

        gradientTextView.paint.shader = textShader
    }
}