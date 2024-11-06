package com.example.partyask

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class ModeExplain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode_reader)

        val mode = intent.getStringExtra("mode_key")

        val modetitle = findViewById<TextView>(R.id.modetitle)
        val explanationmode = findViewById<TextView>(R.id.explanationmode)

        when (mode) {
            "guess_who" -> {
                modetitle.text = getString(R.string.guess_who_title)
                explanationmode.text = getString(R.string.guess_who_explanation)
            }
            "most_probably" -> {
                modetitle.text = getString(R.string.most_probably_title)
                explanationmode.text = getString(R.string.most_probably_explanation)
            }
            "true_false" -> {
                modetitle.text = getString(R.string.true_false_title)
                explanationmode.text = getString(R.string.true_false_explanation)
            }
            "free_questions" -> {
                modetitle.text = getString(R.string.free_questions_title)
                explanationmode.text = getString(R.string.free_questions_explanation)
            }
            "history_or_lie" -> {
                modetitle.text = getString(R.string.history_or_lie_title)
                explanationmode.text = getString(R.string.history_or_lie_explanation)
            }
        }
    }
}