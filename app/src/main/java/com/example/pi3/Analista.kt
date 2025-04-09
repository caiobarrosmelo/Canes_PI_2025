package com.example.pi3

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class Analista : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analista)

        val spinner: Spinner = findViewById(R.id.spinnerPilar)
        val pilares = arrayOf("Pilar 01", "Pilar 02", "Pilar 03")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, pilares)
    }
}