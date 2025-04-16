package com.example.pi3.coordenador

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.pi3.MainActivity
import com.example.pi3.R

class Coordenador : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordenador)

        val spinner: Spinner = findViewById(R.id.spinnerPilar)
        val pilares = arrayOf("Pilar 01", "Pilar 02", "Pilar 03")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, pilares)

        val btnInicio: Button = findViewById(R.id.btnInicio)
        btnInicio.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
} 