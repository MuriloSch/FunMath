package com.dq.funmath

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCounting = findViewById<Button>(R.id.btnCounting)
        val btnArithmetic = findViewById<Button>(R.id.btnArithmetic)
        val btnLargestNumber = findViewById<Button>(R.id.btnLargestNumber)

        btnCounting.setOnClickListener {
            val intent = Intent(this, CountingGameActivity::class.java)
            startActivity(intent)
        }

        btnArithmetic.setOnClickListener {
            val intent = Intent(this, ArithmeticGameActivity::class.java)
            startActivity(intent)
        }

        btnLargestNumber.setOnClickListener {
            val intent = Intent(this, LargestNumberActivity::class.java)
            startActivity(intent)
        }
    }
}