package com.example.dicerollerxml

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val diceImage1: ImageView = findViewById(R.id.dice1)
        val diceImage2: ImageView = findViewById(R.id.dice2)
        val rollButton: Button = findViewById(R.id.rollButton)
        val bottomText: TextView = findViewById(R.id.messageText)

        rollButton.setOnClickListener {
            val dice1 = (1..6).random()
            val dice2 = (1..6).random()
            val drawable1 = when (dice1) {
                1 -> R.drawable.dice_1
                2 -> R.drawable.dice_2
                3 -> R.drawable.dice_3
                4 -> R.drawable.dice_4
                5 -> R.drawable.dice_5
                else -> R.drawable.dice_6
            }
            val drawable2 = when (dice2) {
                1 -> R.drawable.dice_1
                2 -> R.drawable.dice_2
                3 -> R.drawable.dice_3
                4 -> R.drawable.dice_4
                5 -> R.drawable.dice_5
                else -> R.drawable.dice_6
            }
            diceImage1.setImageResource(drawable1)
            diceImage2.setImageResource(drawable2)

            bottomText.apply {
                text = getString(
                    if (dice1 == dice2) R.string.twins else R.string.zonk
                )
                setBackgroundColor(getColor(R.color.purple_500))
                setTextColor(getColor(R.color.white))
                setPadding(16, 16, 16, 16)
                visibility = TextView.VISIBLE
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}