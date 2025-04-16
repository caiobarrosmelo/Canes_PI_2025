package com.example.pi3.apoio

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.pi3.R

class Analista : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analista)

        val spinner: Spinner = findViewById(R.id.spinnerPilar)
        val pilares = arrayOf("Pilar 01", "Pilar 02", "Pilar 03")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, pilares)
    }
}